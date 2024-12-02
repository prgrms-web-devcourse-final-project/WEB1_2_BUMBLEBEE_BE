package roomit.main.domain.studyroom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.repository.ReservationRepository;
import roomit.main.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.main.domain.studyroom.dto.request.FindAvailableStudyRoomRequest;
import roomit.main.domain.studyroom.dto.request.UpdateStudyRoomRequest;
import roomit.main.domain.studyroom.dto.response.FindPossibleStudyRoomResponse;
import roomit.main.domain.studyroom.dto.response.RecentStudyRoomResponse;
import roomit.main.domain.studyroom.dto.response.StudyRoomResponse;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.repository.StudyRoomRepository;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;

import java.util.List;
import java.util.Optional;
import roomit.main.global.service.ImageService;

@Service
@RequiredArgsConstructor
public class StudyRoomService {

    private final StudyRoomRepository studyRoomRepository;
    private final WorkplaceRepository workplaceRepository;
    private final ImageService imageService;

    // 스터디룸 만드는 메서드
    @Transactional
    public void createStudyRoom(Long workPlaceId,CreateStudyRoomRequest request) {
        Workplace workplace = workplaceRepository.findById(workPlaceId)
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);
        StudyRoom createdStudyRoom = request.toEntity(imageService);
        createdStudyRoom.setWorkPlace(workplace);
        studyRoomRepository.save(createdStudyRoom);
    }

    // 사업장ID에 따른 스터디룸 리스트 조회 ( 스터디룸 상세페이지 - 룸 선택 탭 )
    @Transactional(readOnly = true)
    public List<StudyRoomResponse> findStudyRoomsByWorkPlaceId(Long workplaceId) {
        return studyRoomRepository.findStudyRoomsByWorkPlaceId(workplaceId);
    }

    // 스터디룸 단건 조회
    @Transactional(readOnly = true)
    public StudyRoom getStudyRoom(Long studyRoomid) {
        return studyRoomRepository.findById(studyRoomid)
                .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);
    }

    // 스터디룸 업데이트
    @Transactional
    public void updateStudyRoom(Long studyRoomId,UpdateStudyRoomRequest request) {
        StudyRoom existingStudyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);
        request.updatedStudyRoom(existingStudyRoom);
    }

    // 스터디룸 정보 삭제
    @Transactional
    public void deleteStudyRoom(Long studyRoomId) {
        StudyRoom existingStudyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);
        studyRoomRepository.delete(existingStudyRoom);
    }

    // 예약가능한 스터디룸 조회
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
