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
import roomit.main.domain.workplace.dto.reponse.WorkplaceGetRequest;
import roomit.main.domain.workplace.dto.reponse.WorkplaceRequest;
import roomit.main.domain.workplace.dto.request.WorkplaceGetResponse;
import roomit.main.domain.workplace.dto.request.WorkplaceResponse;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.WorkplaceAddress;
import roomit.main.domain.workplace.entity.value.WorkplaceName;
import roomit.main.domain.workplace.entity.value.WorkplacePhoneNumber;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkplaceService {

    private final WorkplaceRepository workplaceRepository;
    private final BusinessRepository businessRepository;
    private final WebClient webClient;

    public List<WorkplaceGetResponse> readAllWorkplaces(WorkplaceGetRequest request, double myLat, double myLon) {
        List<Object[]> results = workplaceRepository.findAllByLatitudeAndLongitudeWithDistance(
                myLat, myLon,
                request.bottomRight().getLatitude().doubleValue(),
                request.topLeft().getLatitude().doubleValue(),
                request.topLeft().getLongitude().doubleValue(),
                request.bottomRight().getLongitude().doubleValue());

        return results.stream()
                .map(result -> new WorkplaceGetResponse(
                        ((Number) result[0]).longValue(),
                        (String) result[1],
                        (String) result[2],
                        (String) result[3],
                        (String) result[4],
                        (String) result[5],
                        ((Timestamp) result[6]).toLocalDateTime(),
                        ((Timestamp) result[7]).toLocalDateTime(),
                        ((Timestamp) result[8]).toLocalDateTime(),
                        (BigDecimal) result[10],
                        (BigDecimal) result[9],
                        ((Number) result[11]).doubleValue()
                ))
                .toList();
    }


    public WorkplaceResponse readWorkplace(Long workplaceId) {
        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

        return new WorkplaceResponse(workplace);
    }

    @Transactional
    public void createWorkplace(WorkplaceRequest workplaceDto, Long id) {

        if (workplaceRepository.getWorkplaceByWorkplaceName(new WorkplaceName(workplaceDto.workplaceName())) != null ||
                workplaceRepository.getWorkplaceByWorkplacePhoneNumber(new WorkplacePhoneNumber(workplaceDto.workplacePhoneNumber())) != null ||
                workplaceRepository.getWorkplaceByWorkplaceAddress(new WorkplaceAddress(workplaceDto.workplaceAddress())) != null) {
            throw ErrorCode.WORKPLACE_NOT_REGISTERED.commonException();
        }

        Map<String, BigDecimal> coordinates = getStringBigDecimalMap(workplaceDto);

        try {
            Business business = businessRepository.findById(id).orElseThrow(ErrorCode.BUSINESS_NOT_FOUND::commonException);
            Workplace workplace = workplaceDto.toEntity(coordinates.get("latitude"), coordinates.get("longitude"), business);
            workplace.changeStarSum(0L);
            workplaceRepository.save(workplace);
        } catch (InvalidDataAccessApiUsageException e) {
            throw ErrorCode.WORKPLACE_INVALID_REQUEST.commonException();
        } catch (Exception e) {
            throw ErrorCode.WORKPLACE_NOT_REGISTERED.commonException();
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
            throw ErrorCode.BUSINESS_NOT_DELETE.commonException();
        }
    }

    public List<WorkplaceResponse> findWorkplacesByBusinessId(Long businessId) {
        List<Workplace> workplaces = workplaceRepository.findByBusiness_BusinessId(businessId);

        if (workplaces.isEmpty()) {
            throw ErrorCode.STUDYROOM_NOT_FOUND.commonException();
        }

        return toResponseDto(workplaces);
    }

    private List<WorkplaceResponse> toResponseDto(List<Workplace> workplaces) {
        List<WorkplaceResponse> workplaceDtoList = new ArrayList<>();
        for (Workplace workplace : workplaces) {
            workplaceDtoList.add(new WorkplaceResponse(workplace));
        }
        return workplaceDtoList;
    }

    protected Map<String, BigDecimal> getStringBigDecimalMap(WorkplaceRequest workplaceDto) {
        try {
            return geoCording(workplaceDto.workplaceAddress());
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

