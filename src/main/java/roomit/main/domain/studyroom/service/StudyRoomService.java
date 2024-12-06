package roomit.main.domain.studyroom.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.repository.ReservationRepository;
import roomit.main.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.main.domain.studyroom.dto.request.FindAvailableStudyRoomRequest;
import roomit.main.domain.studyroom.dto.request.UpdateStudyRoomRequest;
import roomit.main.domain.studyroom.dto.response.FindPossibleStudyRoomResponse;
import roomit.main.domain.studyroom.dto.response.ReservationPossibleStudyRoomResponse;
import roomit.main.domain.studyroom.dto.response.StudyRoomListResponse;
import roomit.main.domain.studyroom.dto.response.StudyRoomResponse;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.repository.StudyRoomRepository;
import roomit.main.domain.workplace.dto.response.DistanceWorkplaceResponse;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;
import roomit.main.global.service.FileLocationService;
import roomit.main.global.service.ImageService;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudyRoomService {

    private final StudyRoomRepository studyRoomRepository;
    private final WorkplaceRepository workplaceRepository;
    private final FileLocationService fileLocationService;
    private final ReservationRepository reservationRepository;
    private final ImageService imageService;

    // 스터디룸 등록
    @Transactional
    public Long createStudyRoom(Long workPlaceId,
                                CreateStudyRoomRequest request,
                                Long businessId) {
        Workplace workplace = workplaceRepository.findById(workPlaceId)
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

        //본인의 사업장인지 검사
        if(!workplace.getBusiness().getBusinessId().equals(businessId)) {
            throw ErrorCode.NOT_OWNER_OF_STUDYROOM.commonException();
        }

        try {
            StudyRoom studyRoom = studyRoomRepository.save(request.toEntity(workplace));
            String imageUrl = "workplace-" + workPlaceId + "/studyroom-" + studyRoom.getStudyRoomId();
            studyRoom.changeStudyRoomImageUrl(imageService.createImageUrl(imageUrl));
            return studyRoom.getStudyRoomId();
        }catch (Exception e) {
            throw ErrorCode.STUDYROOM_NOT_REGISTERD.commonException();
        }
    }

    // 사업장ID에 따른 스터디룸 리스트 조회 ( 스터디룸 상세페이지 - 룸 선택 탭 )
    @Transactional(readOnly = true)
    public List<StudyRoomListResponse> findStudyRoomsByWorkPlaceId(Long workplaceId) {
        List<StudyRoomListResponse> studyRoomListResponses = new ArrayList<>();
        try {
            for (StudyRoom studyRoom : studyRoomRepository.findStudyRoomsByWorkPlaceId(workplaceId)) {
                studyRoomListResponses.add(new StudyRoomListResponse(studyRoom, fileLocationService));
            }
        }catch (Exception e) {
            throw ErrorCode.STUDYROOM_NOT_FOUND.commonException();
        }
        return studyRoomListResponses;
    }

    // 스터디룸 수정
    @Transactional
    public void updateStudyRoom(Long studyRoomId,
                                UpdateStudyRoomRequest request,
                                Long businessId) {
        StudyRoom existingStudyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);

        //본인의 사업장인지 검사
        if(!existingStudyRoom.getWorkPlace().getBusiness().getBusinessId().equals(businessId)) {
            throw ErrorCode.NOT_OWNER_OF_STUDYROOM.commonException();
        }

        try {
            existingStudyRoom.updatedStudyRoom(request);
        }catch (Exception e) {
            throw ErrorCode.STUDYROOM_NOT_MODIFY.commonException();
        }
    }

    // 스터디룸 삭제
    @Transactional
    public void deleteStudyRoom(Long studyRoomId, Long businessId) {
        StudyRoom existingStudyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);

        //본인의 사업장인지 검사
        if(!existingStudyRoom.getWorkPlace().getBusiness().getBusinessId().equals(businessId)) {
            throw ErrorCode.NOT_OWNER_OF_STUDYROOM.commonException();
        }
        try {
            studyRoomRepository.delete(existingStudyRoom);
        }catch (Exception e) {
            throw ErrorCode.STUDYROOM_NOT_DELETE.commonException();
        }
    }

    // 스터디룸 상세 정보 조회
    @Transactional(readOnly = true)
    public StudyRoomResponse getStudyRoom(Long studyRoomId) {

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
            .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);

        return new StudyRoomResponse(studyRoom, fileLocationService);
    }

    //스터디룸 예약 가능한 시간대 조회
    @Transactional(readOnly = true)
    public ReservationPossibleStudyRoomResponse getPossibleReservation(Long studyRoomId,
                                                                       LocalDate checkDate) {

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
            .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);

        List<String> existingTime = new ArrayList<>();
        List<String> operatingHours = new ArrayList<>();
        LocalTime WorkplaceStartTime = studyRoom.getWorkPlace().getWorkplaceStartTime();
        LocalTime WorkplaceEndTime = studyRoom.getWorkPlace().getWorkplaceEndTime();

        //예약되어 있는 시간 검사
        for (Reservation existingReservation : reservationRepository.findReservationsByStudyRoomAndDate(studyRoomId, checkDate)) {
            LocalDateTime startTime = existingReservation.getStartTime();
            LocalDateTime endTime = existingReservation.getEndTime();

            // 시작 시간과 종료 시간에서 시간만 추출해 저장
            while (!startTime.isAfter(endTime)) {
                existingTime.add(startTime.toLocalTime().withMinute(0).toString()); // HH:mm 형식으로 저장
                startTime = startTime.plusHours(1); // 한 시간씩 증가
            }
        }

        //운영 시간 생성
        while(!WorkplaceStartTime.isAfter(WorkplaceEndTime)){
            operatingHours.add(WorkplaceStartTime.toString());
            WorkplaceStartTime = WorkplaceStartTime.plusHours(1);
            if(WorkplaceStartTime.equals(WorkplaceEndTime)){
                break;
            }

        }

        List<String> possibleTime = operatingHours.stream()
                                                  .filter(i -> !existingTime.contains(i))
                                                  .toList();

        return new ReservationPossibleStudyRoomResponse(studyRoom,
                                                        possibleTime,
                                                        studyRoom.getWorkPlace().getWorkplaceStartTime(),
                                                        studyRoom.getWorkPlace().getWorkplaceEndTime());
    }


    // 예약가능한 스터디룸 조회
    @Transactional(readOnly = true)
    public List<FindPossibleStudyRoomResponse> findAvailableStudyRooms(FindAvailableStudyRoomRequest request,
        List<DistanceWorkplaceResponse> searchWorkplace) {

        // 검색된 Workplace ID
        List<Long> workplaceIds = searchWorkplace.stream()
            .map(DistanceWorkplaceResponse::workplaceId)
            .toList();

        List<StudyRoom> studyRooms = studyRoomRepository.findByWorkPlaceId(workplaceIds, request.reservationCapacity());

        // 예약이 있는 경우에 대해서만 필터링
        return studyRooms.stream()
            .filter(studyRoom -> studyRoom.getReservations().stream()
                .noneMatch(reservation ->
                    reservation.getStartTime().isBefore(request.endDateTime()) &&
                        reservation.getEndTime().isAfter(request.startDateTime())
                )) // 예약이 겹치지 않는 경우만 필터링
            .map(studyRoom -> {
                // studyRoom의 workplaceId와 searchWorkplace의 workplaceId를 비교하여 일치하는 distance를 찾음
                double distance = searchWorkplace.stream()
                    .filter(workplace -> workplace.workplaceId().equals(studyRoom.getWorkPlace().getWorkplaceId()))
                    .map(DistanceWorkplaceResponse::distance)
                    .findFirst()
                    .orElse(0.0); // 기본값 0.0, 일치하는 경우가 없으면 0.0을 반환

                return new FindPossibleStudyRoomResponse(studyRoom, distance);
            })
            .collect(Collectors.toList());
    }

}
