package roomit.main.domain.workplace.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
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
import roomit.main.domain.workplace.entity.value.WorkplaceAddress;
import roomit.main.domain.workplace.entity.value.WorkplaceName;
import roomit.main.domain.workplace.entity.value.WorkplacePhoneNumber;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;
import roomit.main.global.exception.CommonException;
import roomit.main.global.service.FileLocationService;
import roomit.main.global.service.ImageService;
import roomit.main.global.util.PointUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkplaceService {

    private final WorkplaceRepository workplaceRepository;
    private final BusinessRepository businessRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final WebClient webClient;
    private final ImageService imageService;
    private final FileLocationService fileLocationService;
    private final PointUtil pointUtil;

    public List<WorkplaceAllResponse> readAllWorkplaces(WorkplaceGetRequest request) {
        String referencePoint = String.format("POINT(%f %f)", request.longitude(), request.latitude());
        String area = String.format(
                "POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
                request.bottomLeft().getLongitude().doubleValue(), request.bottomLeft().getLatitude().doubleValue(),
                request.topRight().getLongitude().doubleValue(), request.bottomLeft().getLatitude().doubleValue(),
                request.topRight().getLongitude().doubleValue(), request.topRight().getLatitude().doubleValue(),
                request.bottomLeft().getLongitude().doubleValue(), request.topRight().getLatitude().doubleValue(),
                request.bottomLeft().getLongitude().doubleValue(), request.bottomLeft().getLatitude().doubleValue()
        );

        log.info(area);

        List<Object[]> results = workplaceRepository.findAllWithinArea(referencePoint, area);

        if (results.isEmpty()) {
            return new ArrayList<>();
        }

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

        Map<String, Double> coordinates = getStringBigDecimalMap(workplaceDto);
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

        Map<String, Double> coordinates = getStringBigDecimalMap(workplaceDto);
        Point newLocation = pointUtil.createPoint(coordinates.get("longitude"), coordinates.get("latitude"));

        try {
            workplace.changeWorkplaceName(new WorkplaceName(workplaceDto.workplaceName()));
            workplace.changeWorkplaceDescription(workplaceDto.workplaceDescription());
            workplace.changeWorkplaceAddress(new WorkplaceAddress(workplaceDto.workplaceAddress()));
            workplace.changeWorkplacePhoneNumber(new WorkplacePhoneNumber(workplaceDto.workplacePhoneNumber()));
            workplace.changeWorkplaceStartTime(workplaceDto.workplaceStartTime());
            workplace.changeWorkplaceEndTime(workplaceDto.workplaceEndTime());
            workplace.changeLocation(newLocation);

            workplaceRepository.save(workplace);
        } catch (Exception e) {
            throw ErrorCode.WORKPLACE_NOT_MODIFIED.commonException();
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

    protected Map<String, Double> getStringBigDecimalMap(WorkplaceRequest workplaceDto) {
        try {
            String[] parts = workplaceDto.workplaceAddress().split(",", 2);
            String roadAddress = parts[0].trim();
            String detailAddress = (parts.length > 1) ? parts[1].trim() : "";
            return geoCording(roadAddress);
        } catch (Exception e) {
            throw ErrorCode.WORKPLACE_INVALID_ADDRESS.commonException();
        }
    }

    private Map<String, Double> geoCording(String address) {
        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/address.json")
                            .queryParam("query", address)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // 동기식 호출

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode document = objectMapper.readTree(response).path("documents").get(0);

            Double latitude = Double.valueOf(document.path("y").asText());
            Double longitude = Double.valueOf(document.path("x").asText());

            return Map.of(
                    "latitude", latitude,
                    "longitude", longitude
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON response", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract coordinates from response", e);
        }
    }

    @Transactional(readOnly = true)
    public List<DistanceWorkplaceResponse> findNearbyWorkplaces(String address, double maxDistance) {
        // 1. 주소를 좌표로 변환
        Map<String, Double> coordinates = geoCording(address);
        Double latitude = coordinates.get("latitude");
        Double longitude = coordinates.get("longitude");

        // 2. 좌표와 거리 기반으로 Workplace 조회
        List<Object[]> results = workplaceRepository.findNearbyWorkplaces(longitude.doubleValue(), latitude.doubleValue(), maxDistance);

        DecimalFormat df = new DecimalFormat("#.##"); // 소수점 2자리 포맷

        return results.stream()
            .map(result -> new DistanceWorkplaceResponse(
                (Long) result[0], // workplaceId
                Double.valueOf(df.format((Double) result[1] / 1000)) // distance
            ))
            .collect(Collectors.toList());
    }
}

