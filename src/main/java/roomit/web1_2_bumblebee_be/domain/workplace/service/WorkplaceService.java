package roomit.web1_2_bumblebee_be.domain.workplace.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceRequest;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceResponse;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplaceAddress;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplaceName;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplacePhoneNumber;
import roomit.web1_2_bumblebee_be.domain.workplace.exception.*;
import roomit.web1_2_bumblebee_be.domain.workplace.repository.WorkplaceRepository;
import roomit.web1_2_bumblebee_be.global.error.ErrorCode;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkplaceService {

    private final WorkplaceRepository workplaceRepository;

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

        try {
            Workplace workplace = workplaceDto.toEntity();
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
        if (workplaceDto == null) {
            throw ErrorCode.WORKPLACE_INVALID_REQUEST.commonException();
        }

        // 필수 필드 검증
        if (!workplaceDto.workplaceStartTime().isBefore(workplaceDto.workplaceEndTime())) {
            throw ErrorCode.WORKPLACE_INVALID_REQUEST.commonException();
        }

        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

        try {
            workplace.changeWorkplaceName(new WorkplaceName(workplaceDto.workplaceName()));
            workplace.changeWorkplaceDescription(workplaceDto.workplaceDescription());
            workplace.changeWorkplaceAddress(new WorkplaceAddress(workplaceDto.workplaceAddress()));
            workplace.changeWorkplacePhoneNumber(new WorkplacePhoneNumber(workplaceDto.workplacePhoneNumber()));
            workplace.changeWorkplaceStartTime(workplaceDto.workplaceStartTime());
            workplace.changeWorkplaceEndTime(workplaceDto.workplaceEndTime());
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

}
