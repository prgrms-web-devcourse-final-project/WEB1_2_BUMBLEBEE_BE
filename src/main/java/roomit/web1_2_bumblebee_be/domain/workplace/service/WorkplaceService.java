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
                .orElseThrow(WorkplaceNotFound::new);

        return new WorkplaceResponse(workplace);
    }

    @Transactional
    public void createWorkplace(WorkplaceRequest workplaceDto) {

        if (workplaceRepository.getWorkplaceByWorkplaceName(new WorkplaceName(workplaceDto.getWorkplaceName())) != null ||
                workplaceRepository.getWorkplaceByWorkplacePhoneNumber(new WorkplacePhoneNumber(workplaceDto.getWorkplacePhoneNumber())) != null ||
                workplaceRepository.getWorkplaceByWorkplaceAddress(new WorkplaceAddress(workplaceDto.getWorkplaceAddress())) != null) {
            throw new RuntimeException("이미 존재합니다");
        }

        try {
            Workplace workplace = workplaceDto.toEntity();
            workplace.changeStarSum(0L);
            workplaceRepository.save(workplace);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new WorkplaceInvalidRequest(); // 예외 감싸기
        } catch (Exception e) {
            throw new WorkplaceNotRegistered();
        }
    }

    @Transactional
    public void updateWorkplace(Long workplaceId, WorkplaceRequest workplaceDto) {
        if (workplaceDto == null) {
            throw new WorkplaceInvalidRequest();
        }

        // 필수 필드 검증
        if (!workplaceDto.getWorkplaceStartTime().isBefore(workplaceDto.getWorkplaceEndTime())) {
            throw new WorkplaceInvalidRequest();
        }

        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(WorkplaceNotFound::new);

        try {
            workplace.changeWorkplaceName(new WorkplaceName(workplaceDto.getWorkplaceName()));
            workplace.changeWorkplaceDescription(workplaceDto.getWorkplaceDescription());
            workplace.changeWorkplaceAddress(new WorkplaceAddress(workplaceDto.getWorkplaceAddress()));
            workplace.changeWorkplacePhoneNumber(new WorkplacePhoneNumber(workplaceDto.getWorkplacePhoneNumber()));
            workplace.changeWorkplaceStartTime(workplaceDto.getWorkplaceStartTime());
            workplace.changeWorkplaceEndTime(workplaceDto.getWorkplaceEndTime());
            workplaceRepository.save(workplace);
        } catch (Exception e) {
            throw new WorkspaceNotModified();
        }
    }

    @Transactional
    public void deleteWorkplace(Long workplaceId) {
        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(WorkplaceNotFound::new);

        try {
            workplaceRepository.delete(workplace);
        } catch (Exception e) {
            throw new WorkplaceNotDelete();
        }
    }

    public List<WorkplaceResponse> findWorkplacesByBusinessId(Long businessId) {
        List<Workplace> workplaces = workplaceRepository.findByBusiness_BusinessId(businessId);

        if (workplaces.isEmpty()) {
            throw new WorkplaceNotFound();
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


    //사진 업로드 및 수정
//    @Transactional
//    public WorkplaceResponse uploadImage(MultipartFile file, Long workplaceId) {
//        if (file == null || file.isEmpty()) {
//            throw WorkplaceException.NOT_UPLOAD_IMAGE.getWorkplaceTaskException();
//        }
//
//        try {
//            // Workplace 조회
//            Workplace workplace = workplaceRepository.findById(workplaceId)
//                    .orElseThrow(() -> WorkplaceException.MEMBER_NOT_FOUND.getWorkplaceTaskException());
//
//            // 파일 데이터 저장
//            workplace.changeProfileImage(file.getBytes());
//            workplace.changeImageType(file.getContentType());
//
//            log.info("Profile image updated successfully for memberId: {}", workplaceId);
//
//            // 저장 후 DTO 반환
//            return new WorkplaceResponse(workplaceRepository.save(workplace));
//        } catch (IOException e) {
//            log.error("Error while processing file upload: {}", e.getMessage(), e);
//            throw WorkplaceException.NOT_UPLOAD_IMAGE.getWorkplaceTaskException();
//        } catch (Exception e) {
//            log.error("Unexpected error: {}", e.getMessage(), e);
//            throw WorkplaceException.NOT_UPLOAD_IMAGE.getWorkplaceTaskException();
//        }
//    }
}
