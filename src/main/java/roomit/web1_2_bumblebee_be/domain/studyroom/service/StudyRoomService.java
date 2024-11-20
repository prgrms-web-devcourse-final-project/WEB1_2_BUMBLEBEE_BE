package roomit.web1_2_bumblebee_be.domain.studyroom.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
import roomit.web1_2_bumblebee_be.domain.reservation.entity.Reservation;
import roomit.web1_2_bumblebee_be.domain.reservation.repository.ReservationRepository;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.StudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.response.StudyRoomResponse;
import roomit.web1_2_bumblebee_be.domain.studyroom.repository.StudyRoomRepository;

import java.util.Optional;
import java.util.List;

@Service

public class StudyRoomService{

    private final StudyRoomRepository studyRoomRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    public StudyRoomService(StudyRoomRepository studyRoomRepository, MemberRepository memberRepository,ReservationRepository reservationRepository) {
        this.studyRoomRepository = studyRoomRepository;
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
    }


    // 회원이 예약한 스터디룸 조회
    /* @Transactional(readOnly=true)
    public List<StudyRoomResponse> searchStudyRoom(StudyRoomRequest studyRoomRequest) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(study)
        return null;
     */
}
