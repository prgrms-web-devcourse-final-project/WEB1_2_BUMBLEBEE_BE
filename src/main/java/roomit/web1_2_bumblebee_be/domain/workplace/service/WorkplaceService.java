package roomit.web1_2_bumblebee_be.domain.workplace.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceRequest;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceResponse;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplaceAddress;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplaceName;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplacePhoneNumber;
import roomit.web1_2_bumblebee_be.domain.workplace.repository.WorkplaceRepository;
import roomit.web1_2_bumblebee_be.global.error.ErrorCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkplaceService {

    private final WorkplaceRepository workplaceRepository;

    private final WebClient webClient;

    public List<WorkplaceResponse> readAllWorkplaces() {
        List<Workplace> workplaceList = workplaceRepository.findAll();

        return toResponseDto(workplaceList);
    }

    public WorkplaceResponse readWorkplace(Long workplaceId) {
        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

        return new WorkplaceResponse(workplace);
    }

    @Transactional
    public void createWorkplace(WorkplaceRequest workplaceDto) {

        if (workplaceRepository.getWorkplaceByWorkplaceName(new WorkplaceName(workplaceDto.workplaceName())) != null ||
                workplaceRepository.getWorkplaceByWorkplacePhoneNumber(new WorkplacePhoneNumber(workplaceDto.workplacePhoneNumber())) != null ||
                workplaceRepository.getWorkplaceByWorkplaceAddress(new WorkplaceAddress(workplaceDto.workplaceAddress())) != null) {
            throw ErrorCode.WORKPLACE_NOT_REGISTERED.commonException();
        }

        Map<String, BigDecimal> coordinates = getStringBigDecimalMap(workplaceDto);

        try {
            Workplace workplace = workplaceDto.toEntity(coordinates.get("latitude"), coordinates.get("longitude"));
            workplace.changeStarSum(0L);
            workplaceRepository.save(workplace);
        } catch (InvalidDataAccessApiUsageException e) {
            throw ErrorCode.WORKPLACE_INVALID_REQUEST.commonException();
        } catch (Exception e) {
            throw ErrorCode.WORKPLACE_NOT_REGISTERED.commonException();
        }
    }

    @Transactional
    public void updateWorkplace(Long workplaceId, WorkplaceRequest workplaceDto) {

        if (workplaceDto == null && !workplaceDto.workplaceStartTime().isBefore(workplaceDto.workplaceEndTime())) {
            throw ErrorCode.WORKPLACE_INVALID_REQUEST.commonException();
        }

        Map<String, BigDecimal> coordinates = getStringBigDecimalMap(workplaceDto);

        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

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
    public void deleteWorkplace(Long workplaceId) {
        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

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

    private Map<String, BigDecimal> getStringBigDecimalMap(WorkplaceRequest workplaceDto) {
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

            return Map.of(
                    "latitude", new BigDecimal(document.path("y").asText()),
                    "longitude", new BigDecimal(document.path("x").asText())
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract coordinates from response", e);
        }
    }
}
