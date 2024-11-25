package roomit.web1_2_bumblebee_be.domain.studyroom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
import roomit.web1_2_bumblebee_be.domain.reservation.entity.Reservation;
import roomit.web1_2_bumblebee_be.domain.reservation.repository.ReservationRepository;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.FindAvailableStudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.UpdateStudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.response.FindPossibleStudyRoomResponse;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.response.RecentStudyRoomResponse;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.response.StudyRoomResponse;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;
import roomit.web1_2_bumblebee_be.domain.studyroom.repository.StudyRoomRepository;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.repository.WorkplaceRepository;
import roomit.web1_2_bumblebee_be.global.error.ErrorCode;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyRoomService {

    private final StudyRoomRepository studyRoomRepository;
    private final ReservationRepository reservationRepository;
    private final WorkplaceRepository workplaceRepository;
    private final MemberRepository memberRepository;
    //private final ReviewRepository reviewRepository;

    // 스터디룸 만드는 메서드
    @Transactional
    public StudyRoomResponse createStudyRoom(CreateStudyRoomRequest request) {
        StudyRoom createdStudyRoom = request.toEntity();
        StudyRoom savedStudyRoom = studyRoomRepository.save(createdStudyRoom);

        return StudyRoomResponse.from(savedStudyRoom);
    }

    // 작업장의 스터디룸 조회
    @Transactional(readOnly = true)
    public List<StudyRoom> findStudyRoomsByWorkplace(Long workplaceId) {
        return studyRoomRepository.findStudyRoomsByWorkPlaceId(workplaceId);
    }

    // 스터디룸 전체 조회
    @Transactional(readOnly = true)
    public List<StudyRoom> getAllStudyRooms() {
        return studyRoomRepository.findAll();
    }

    // 스터디룸 조회 메서드
    @Transactional(readOnly = true)
    public StudyRoom getStudyRoom(Long studyRoomid) {
        return studyRoomRepository.findById(studyRoomid)
                .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);
    }

    // 스터디룸 수정 메서드
    @Transactional
    public void updateStudyRoom(UpdateStudyRoomRequest request) {
        StudyRoom studyRoom = getStudyRoom(request.studyRoomId());
        request.updaedStudyRoom(studyRoom);
        studyRoomRepository.save(studyRoom);
    }

    // 스터디룸 삭제 (Delete)
    @Transactional
    public void deleteStudyRoom(Long studyRoomId) {
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);

        studyRoomRepository.delete(studyRoom);
    }

    // 사업장ID에 따른 스터디룸 리스트 조회 v
    @Transactional(readOnly = true)
    public List<StudyRoom> findStudyRoomsByWorkPlaceId(Long workplaceId) {
        return studyRoomRepository.findStudyRoomsByWorkPlaceId(workplaceId);
    }

    // 최근 예약한 스터디룸 보여주기
    public Optional<RecentStudyRoomResponse> findRecentReservation(Long memberId){
        Optional<Reservation> recentReservationOpt = reservationRepository.findRecentReservationByMemberId(memberId);

        if (recentReservationOpt.isPresent()) {
            Reservation recentReservation = recentReservationOpt.get();

            Long studyRoomId = recentReservation.getStudyRoom().getStudyRoomId();
            StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
                    .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);

            Long workplaceId = studyRoom.getWorkPlaceId().getWorkplaceId();
            Workplace workplace = workplaceRepository.findById(workplaceId)
                    .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);
            return Optional.of(RecentStudyRoomResponse.from(workplace, recentReservation, studyRoom));
        } else {
            return Optional.empty();
        }
    }

    // 사용 가능한 스터디룸 목록 조회
    @Transactional(readOnly = true)
    public List<FindPossibleStudyRoomResponse> findAvailableStudyRooms(FindAvailableStudyRoomRequest request) {
        return studyRoomRepository.findAvailableStudyRooms(
                request.workplaceAddress(),
                request.startTime(),
                request.endTime(),
                request.capacity()
        );
    }
}
