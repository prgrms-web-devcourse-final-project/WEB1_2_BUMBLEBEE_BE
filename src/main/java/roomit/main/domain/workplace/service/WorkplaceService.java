package roomit.main.domain.workplace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.business.repository.BusinessRepository;
import roomit.main.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.repository.StudyRoomRepository;
import roomit.main.domain.workplace.dto.request.WorkplaceGetRequest;
import roomit.main.domain.workplace.dto.request.WorkplaceRequest;
import roomit.main.domain.workplace.dto.response.DistanceWorkplaceResponse;
import roomit.main.domain.workplace.dto.response.WorkplaceAllResponse;
import roomit.main.domain.workplace.dto.response.WorkplaceBusinessResponse;
import roomit.main.domain.workplace.dto.response.WorkplaceCreateResponse;
import roomit.main.domain.workplace.dto.response.WorkplaceDetailResponse;
import roomit.main.domain.workplace.dto.response.WorkplaceResponse;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;
import roomit.main.global.exception.CommonException;
import roomit.main.global.service.FileLocationService;
import roomit.main.global.service.ImageService;
import roomit.main.global.util.PointUtil;

@Service
@RequiredArgsConstructor
public class WorkplaceService {

    private final WorkplaceRepository workplaceRepository;
    private final BusinessRepository businessRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final GeoService geoService;
    private final ImageService imageService;
    private final FileLocationService fileLocationService;
    private final PointUtil pointUtil;

    public List<WorkplaceAllResponse> readAllWorkplaces(WorkplaceGetRequest request) {
        String referencePoint = String.format("POINT(%f %f)", request.longitude(), request.latitude());
        String area = String.format(
                "POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
                request.bottomLeft().getLongitude(), request.bottomLeft().getLatitude(),
                request.topRight().getLongitude(), request.bottomLeft().getLatitude(),
                request.topRight().getLongitude(), request.topRight().getLatitude(),
                request.bottomLeft().getLongitude(), request.topRight().getLatitude(),
                request.bottomLeft().getLongitude(), request.bottomLeft().getLatitude()
        );

        List<Object[]> results = workplaceRepository.findAllWithinArea(referencePoint, area);

        return results.stream()
                .map(result -> {
                    double starSum = ((Number) result[4]).doubleValue();
                    long reviewCount = ((Number) result[5]).longValue();

                    return new WorkplaceAllResponse(
                            ((Number) result[0]).longValue(),
                            (String) result[1],
                            (String) result[2],
                            fileLocationService.getImagesFromFolder((String)result[3]).get(0),
                            (reviewCount == 0) ? 0.0 : starSum / reviewCount,
                            reviewCount,
                            ((Number) result[6]).doubleValue(), // longitude
                            ((Number) result[7]).doubleValue(), // latitude
                            ((Number) result[8]).doubleValue()
                    );
                }).toList();
    }


    public WorkplaceDetailResponse readWorkplace(Long workplaceId) {
        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

        return new WorkplaceDetailResponse(workplace, fileLocationService);
    }

    @Transactional
    public WorkplaceCreateResponse createWorkplace(WorkplaceRequest workplaceDto, Long id) {
        Business business = businessRepository.findById(id).orElseThrow(ErrorCode.BUSINESS_NOT_FOUND::commonException);

        Map<String, Double> coordinates = getStringDoubleMap(workplaceDto);
        Point location = pointUtil.createPoint(coordinates.get("longitude"), coordinates.get("latitude"));

        try {
            Workplace workplace = workplaceDto.toEntity(location, business);
            Workplace savedWorkplace = workplaceRepository.save(workplace);
            String imageUrl = "workplace-" + savedWorkplace.getWorkplaceId();
            savedWorkplace.changeImageUrl(imageService.createImageUrl(imageUrl));

            List<Long> studyroomID = new ArrayList<>();
            studyroomID = saveStudyrooms(workplaceDto, savedWorkplace, studyroomID);

            return new WorkplaceCreateResponse(savedWorkplace.getWorkplaceId(), studyroomID);
        }
        catch (IllegalArgumentException e) {
            throw e;
        } catch (InvalidDataAccessApiUsageException e) {
            throw ErrorCode.WORKPLACE_INVALID_REQUEST.commonException();
        } catch (CommonException e) {
            if (e.getErrorCode() == ErrorCode.STUDYROOM_NOT_REGISTERD) {
                throw e;
            }
            throw e;
        }

    }

    private List<Long> saveStudyrooms(WorkplaceRequest workplaceDto, Workplace savedWorkplace, List<Long> studyroomIDs) {
        try {
            for (CreateStudyRoomRequest studyRoomRequest : workplaceDto.studyRoomList()) {
                StudyRoom studyRoom = studyRoomRepository.save(studyRoomRequest.toEntity(savedWorkplace));
                String imageUrl = "workplace-" + savedWorkplace.getWorkplaceId() + "/studyroom-" + studyRoom.getStudyRoomId();
                studyRoom.changeStudyRoomImageUrl(imageService.createImageUrl(imageUrl));
                studyroomIDs.add(studyRoom.getStudyRoomId());
            }
            return studyroomIDs;
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                throw (IllegalArgumentException)e;
            }
            throw ErrorCode.STUDYROOM_NOT_REGISTERD.commonException();
        }
    }

    @Transactional
    public void updateWorkplace(Long workplaceId, WorkplaceRequest workplaceDto, Long businessId) {
        if (workplaceDto == null && !workplaceDto.workplaceStartTime().isBefore(workplaceDto.workplaceEndTime())) {
            throw ErrorCode.WORKPLACE_INVALID_REQUEST.commonException();
        }

        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

        if (!workplace.getBusiness().getBusinessId().equals(businessId)) {
            throw ErrorCode.BUSINESS_NOT_AUTHORIZED.commonException();
        }

        try {
            workplaceRepository.updateWorkplace(workplaceDto, businessId);
        }catch (Exception e){
            // 기존 오류 코드가 담긴 예외를 그대로 던짐
            if (e instanceof CommonException) {
                throw (CommonException) e;
            } else {
                // 새로운 예외를 던지거나 일반 오류 처리
                throw ErrorCode.WORKPLACE_NOT_MODIFIED.commonException();
            }
        }
    }

    @Transactional
    public void deleteWorkplace(Long workplaceId, Long businessId) {
        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

        if (!workplace.getBusiness().getBusinessId().equals(businessId)) {
            throw ErrorCode.BUSINESS_NOT_AUTHORIZED.commonException();
        }

        try {
            workplaceRepository.delete(workplace);
        } catch (Exception e) {
            throw ErrorCode.WORKPLACE_NOT_DELETE.commonException();
        }
    }

    public WorkplaceBusinessResponse findWorkplacesByBusinessId(Long businessId) {
        List<Workplace> workplaces = workplaceRepository.findByBusinessId(businessId);

        Business business = businessRepository.findById(businessId).orElseThrow(ErrorCode.BUSINESS_NOT_FOUND::commonException);

        if (workplaces.isEmpty()) {
            return null;
        }

        return toResponseDto(workplaces, businessId, business.getBusinessName());
    }

    private WorkplaceBusinessResponse toResponseDto(List<Workplace> workplaces, Long businessId, String businessName) {
        List<WorkplaceResponse> workplaceDtoList = new ArrayList<>();
        for (Workplace workplace : workplaces) {
            workplaceDtoList.add(new WorkplaceResponse(workplace, fileLocationService));
        }

        return new WorkplaceBusinessResponse(businessId, businessName, workplaceDtoList);
    }


    @Transactional(readOnly = true)
    public List<DistanceWorkplaceResponse> findNearbyWorkplaces(String address, double maxDistance) {
        // 1. 주소를 좌표로 변환
        Map<String, Double> coordinates = geocoding(address);
        Double latitude = coordinates.get("latitude");
        Double longitude = coordinates.get("longitude");

        // 위도, 경도가 유효한지 확인 (Optional)
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("위도나 경도가 범위를 벗어났습니다.");
        }

        // 2. 좌표와 거리 기반으로 Workplace 조회
        List<DistanceWorkplaceResponse> results = workplaceRepository.findNearbyWorkplaces(longitude, latitude, maxDistance);

        return results.stream()
            .map(result -> new DistanceWorkplaceResponse(
                result.workplaceId(), // workplaceId
                result.distance()
            ))
            .collect(Collectors.toList());
    }

    public Map<String, Double> getStringDoubleMap(WorkplaceRequest workplaceDto) {
        return geoService.parseAddress(workplaceDto.workplaceAddress());
    }

    public Map<String, Double> geocoding(String address) {
        return geoService.geocoding(address);
    }
}

