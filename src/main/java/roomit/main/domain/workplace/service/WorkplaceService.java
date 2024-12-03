package roomit.main.domain.workplace.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.business.repository.BusinessRepository;
import roomit.main.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.repository.StudyRoomRepository;
import roomit.main.domain.workplace.dto.request.WorkplaceGetRequest;
import roomit.main.domain.workplace.dto.request.WorkplaceRequest;
import roomit.main.domain.workplace.dto.response.WorkplaceAllResponse;
import roomit.main.domain.workplace.dto.response.WorkplaceBusinessResponse;
import roomit.main.domain.workplace.dto.response.WorkplaceDetailResponse;
import roomit.main.domain.workplace.dto.response.WorkplaceResponse;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.WorkplaceAddress;
import roomit.main.domain.workplace.entity.value.WorkplaceName;
import roomit.main.domain.workplace.entity.value.WorkplacePhoneNumber;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;
import roomit.main.global.exception.CommonException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import roomit.main.global.service.ImageService;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkplaceService {

    private final WorkplaceRepository workplaceRepository;
    private final BusinessRepository businessRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final WebClient webClient;
    private final ImageService imageService;

    public List<WorkplaceAllResponse> readAllWorkplaces(WorkplaceGetRequest request) {
        List<Object[]> results = workplaceRepository.findAllByLatitudeAndLongitudeWithDistance(
                request.latitude(),
                request.longitude(),
                request.bottomLeft().getLatitude().doubleValue(),
                request.topRight().getLatitude().doubleValue(),
                request.bottomLeft().getLongitude().doubleValue(),
                request.topRight().getLongitude().doubleValue()
        );

        List<WorkplaceAllResponse> responseList = results.stream()
                .map(result -> {
                    double starSum = ((Number) result[4]).doubleValue();
                    long reviewCount = ((Number) result[5]).longValue();
                    return new WorkplaceAllResponse(
                            ((Number) result[0]).longValue(),
                            (String) result[1],
                            (String) result[2],
                            (String) result[3],
                            (reviewCount == 0) ? 0.0 : starSum / reviewCount,
                            reviewCount,
                            BigDecimal.valueOf(((Number) result[6]).doubleValue()),  // latitude
                            BigDecimal.valueOf(((Number) result[7]).doubleValue()),  // longitude
                            ((Number) result[8]).doubleValue()
                    );
                }).toList();

        return responseList;
    }


    public WorkplaceDetailResponse readWorkplace(Long workplaceId) {
        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

        return new WorkplaceDetailResponse(workplace);
    }

    @Transactional
    public void createWorkplace(WorkplaceRequest workplaceDto, Long id) {
        Business business = businessRepository.findById(id).orElseThrow(ErrorCode.BUSINESS_NOT_FOUND::commonException);

        Map<String, BigDecimal> coordinates = getStringBigDecimalMap(workplaceDto);

        try {
            Workplace workplace = workplaceDto.toEntity(coordinates.get("latitude"), coordinates.get("longitude"), business, imageService);
            Workplace savedWorkplace = workplaceRepository.save(workplace);

            saveStudyrooms(workplaceDto, savedWorkplace);
        }
        catch (IllegalArgumentException e) {
            throw new CommonException(ErrorCode.WORKPLACE_INVALID_REQUEST);
        } catch (InvalidDataAccessApiUsageException e) {
            throw ErrorCode.WORKPLACE_INVALID_REQUEST.commonException();
        } catch (CommonException e) {
            if (e.getErrorCode() == ErrorCode.STYDYROOM_NOT_REGISTERD) {
                throw e;
            }
            throw e;
        }

    }

    private void saveStudyrooms(WorkplaceRequest workplaceDto, Workplace savedWorkplace) {
        try {
            for (CreateStudyRoomRequest studyRoomRequest : workplaceDto.studyRoomList()) {
                StudyRoom studyRoom = studyRoomRequest.toEntity(imageService);
                studyRoom.setWorkPlace(savedWorkplace); // 스터디룸에 사업장 연결
                studyRoomRepository.save(studyRoom);
            }
        } catch (Exception e) {
            throw ErrorCode.STYDYROOM_NOT_REGISTERD.commonException();
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

        Map<String, BigDecimal> coordinates = getStringBigDecimalMap(workplaceDto);

        try {
            workplace.changeWorkplaceName(new WorkplaceName(workplaceDto.workplaceName()));
            workplace.changeWorkplaceDescription(workplaceDto.workplaceDescription());
            workplace.changeWorkplaceAddress(new WorkplaceAddress(workplaceDto.workplaceAddress()));
            workplace.changeWorkplacePhoneNumber(new WorkplacePhoneNumber(workplaceDto.workplacePhoneNumber()));
            workplace.changeWorkplaceStartTime(workplaceDto.workplaceStartTime());
            workplace.changeWorkplaceEndTime(workplaceDto.workplaceEndTime());
            workplace.changeLatitude(coordinates.get("latitude"));
            workplace.changeLongitude(coordinates.get("longitude"));
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
            throw ErrorCode.WORKPLACE_NOT_FOUND.commonException();
        }

        return toResponseDto(workplaces, businessId, business.getBusinessName());
    }

    private WorkplaceBusinessResponse toResponseDto(List<Workplace> workplaces, Long businessId, String businessName) {
        List<WorkplaceResponse> workplaceDtoList = new ArrayList<>();
        for (Workplace workplace : workplaces) {
            workplaceDtoList.add(new WorkplaceResponse(workplace));
        }

        return new WorkplaceBusinessResponse(businessId, businessName, workplaceDtoList);
    }

    protected Map<String, BigDecimal> getStringBigDecimalMap(WorkplaceRequest workplaceDto) {
        try {
            String[] parts = workplaceDto.workplaceAddress().split(",", 2);
            String roadAddress = parts[0].trim();
            String detailAddress = (parts.length > 1) ? parts[1].trim() : "";
            return geoCording(roadAddress);
        } catch (Exception e) {
            throw ErrorCode.WORKPLACE_INVALID_ADDRESS.commonException();
        }
    }

    private Map<String, BigDecimal> geoCording(String address) {
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

            BigDecimal latitude = new BigDecimal(document.path("y").asText());
            BigDecimal longitude = new BigDecimal(document.path("x").asText());

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
}

