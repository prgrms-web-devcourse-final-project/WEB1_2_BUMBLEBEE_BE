package roomit.web1_2_bumblebee_be.domain.studyroom.service;

import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
import roomit.web1_2_bumblebee_be.domain.reservation.entity.Reservation;
import roomit.web1_2_bumblebee_be.domain.reservation.repository.ReservationRepository;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.FindPossibleStudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.StudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.UpdateStudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.response.FindPossibleStudyRoomResponse;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.response.StudyRoomResponse;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;
import roomit.web1_2_bumblebee_be.domain.studyroom.exception.StudyRoomNotFound;
import roomit.web1_2_bumblebee_be.domain.studyroom.repository.StudyRoomRepository;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.exception.WorkplaceNotFound;
import roomit.web1_2_bumblebee_be.domain.workplace.repository.WorkplaceRepository;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyRoomService {

    private final StudyRoomRepository studyRoomRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final WorkplaceRepository workplaceRepository;
    //private final ReviewRepository reviewRepository;

    // 스터디룸 만드는 메서드
    @Transactional
    public void createStudyRoom(CreateStudyRoomRequest request) {
        Workplace workPlace = workplaceRepository.findById(request.getWorkplaceId())
            .orElseThrow(WorkplaceNotFound::new);

        StudyRoom studyRoom = request.toEntity(workPlace);

        studyRoomRepository.save(studyRoom);
    }

    // 스터디룸 전체 조회
    @Transactional(readOnly = true)
    public List<StudyRoom> getAllStudyRooms() {
        return studyRoomRepository.findAll();
    }

    // 스터디룸 조회 메서드
    @Transactional(readOnly = true)
    public StudyRoom getStudyRoom(Long id) {
        return studyRoomRepository.findById(id)
            .orElseThrow(StudyRoomNotFound::new);
    }

    // 스터디룸 수정 메서드
    @Transactional
    public void updateStudyRoom(UpdateStudyRoomRequest request) {
        StudyRoom existingStudyRoom = studyRoomRepository.findById(request.getStudyRoomId())
                .orElseThrow(()-> new IllegalArgumentException(request.getStudyRoomId()+"의 ID를 가진 스터디룸이 존재하지않습니다."));

        StudyRoom updatedStudyRoom = UpdateStudyRoomRequest.toEntity(request, existingStudyRoom);

        studyRoomRepository.save(updatedStudyRoom);
    }

    // 스터디룸 삭제 (Delete)
    @Transactional
    public void deleteStudyRoom(Long studyRoomId) {
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
            .orElseThrow(StudyRoomNotFound::new);

        studyRoomRepository.delete(studyRoom);
    }

    // 사용 가능한 스터디룸 목록 조회
    @Transactional(readOnly = true)
    public List<FindPossibleStudyRoomResponse> findAvailableStudyRooms(FindPossibleStudyRoomRequest request) {
        List<StudyRoom> studyRooms = studyRoomRepository.findAvailableStudyRooms(
            request.getWorkplaceAddress(),
            request.getStartTime(),
            request.getEndTime(),
            request.getCapacity()
        );

        return studyRooms.stream()
            .map(studyRoom -> FindPossibleStudyRoomResponse.builder()
                .workplaceName(studyRoom.getWorkPlaceId().getWorkplaceName())
                .workplaceAddress(studyRoom.getWorkPlaceId().getWorkplaceAddress())
                .studyRoomTitle(studyRoom.getTitle())
                .studyRoomCapacity(studyRoom.getCapacity())
                .studyRoomPrice(studyRoom.getPrice())
                //.averageReviewScore(workPlace.getstarSum()) // 이후 review수만큼 나누기
                .imageUrl(studyRoom.getImageUrl())
                .build())
            .collect(Collectors.toList());
    }
}
