package roomit.web1_2_bumblebee_be.domain.workplace.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceRequest;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceResponse;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.exception.WorkplaceException;
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

        List<WorkplaceResponse> workplaceDtoList = new ArrayList<>();
        for (Workplace workplace : workplaceList) {
            workplaceDtoList.add(new WorkplaceResponse(workplace));
        }
        return workplaceDtoList;
    }

    public WorkplaceResponse readWorkplace(Long workplaceId) {
        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(() -> WorkplaceException.WORKPLACE_NOT_FOUND.getWorkplaceTaskException());

        return new WorkplaceResponse(workplace);
    }

    @Transactional
    public void createWorkplace(WorkplaceRequest workplaceDto) {
        String workplaceName = workplaceDto.getWorkplaceName();
        String workplacePhoneNumber = workplaceDto.getWorkplacePhoneNumber();
        String workplaceDescription = workplaceDto.getWorkplaceDescription();
        String workplaceAddress = workplaceDto.getWorkplaceAddress();

        if (workplaceName == null || workplacePhoneNumber == null || workplaceDescription == null || workplaceAddress == null) {
            throw new RuntimeException("입력을 다시 해주세요");
        }

        if (workplaceRepository.getWorkplaceByWorkplaceName(workplaceDto.getWorkplaceName()) != null
        || workplaceRepository.getWorkplaceByWorkplacePhoneNumber(workplaceDto.getWorkplacePhoneNumber()) !=null
        || workplaceRepository.getWorkplaceByWorkplaceAddress(workplaceDto.getWorkplaceAddress()) != null) {
            throw new RuntimeException("이미 존재합니다");
        }

//        uploadImage(workplaceDto.getProfileImage())

        try {
            Workplace workplace = workplaceDto.toEntity();
            workplace.changeStarSum(0L);
            workplaceRepository.save(workplace);
        } catch (Exception e) {
            throw WorkplaceException.WORKPLACE_NOT_REGISTERED.getWorkplaceTaskException();
        }
    }

    @Transactional
    public void updateWorkplace(Long workplaceId, WorkplaceRequest workplaceDto) {
        if (workplaceDto == null) {
            throw WorkplaceException.WORKPLACE_INVALID_REQUEST.getWorkplaceTaskException();
        }

        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(() -> WorkplaceException.WORKPLACE_NOT_FOUND.getWorkplaceTaskException());

        try {
            workplace.changeWorkplaceName(workplaceDto.getWorkplaceName());
            workplace.changeWorkplaceDescription(workplaceDto.getWorkplaceDescription());
            workplace.changeWorkplaceAddress(workplaceDto.getWorkplaceAddress());
            workplace.changeWorkplacePhoneNumber(workplaceDto.getWorkplacePhoneNumber());
            workplace.changeWorkplaceStartTime(workplaceDto.getWorkplaceStartTime());
            workplace.changeWorkplaceEndTime(workplaceDto.getWorkplaceEndTime());
            workplaceRepository.save(workplace);
        } catch (Exception e) {
            throw WorkplaceException.WORKPLACE_NOT_MODIFIED.getWorkplaceTaskException();
        }
    }

    @Transactional
    public void deleteWorkplace(Long workplaceId) {
        workplaceRepository.findById(workplaceId)
                .orElseThrow(() -> WorkplaceException.WORKPLACE_NOT_FOUND.getWorkplaceTaskException());

        workplaceRepository.deleteById(workplaceId);
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
