package roomit.main.domain.studyroom.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.main.domain.studyroom.dto.request.FindAvailableStudyRoomRequest;
import roomit.main.domain.studyroom.dto.request.UpdateStudyRoomRequest;
import roomit.main.domain.studyroom.dto.response.FindPossibleStudyRoomResponse;
import roomit.main.domain.studyroom.dto.response.ReservationPossibleStudyRoomResponse;
import roomit.main.domain.studyroom.dto.response.StudyRoomListResponse;
import roomit.main.domain.studyroom.dto.response.StudyRoomResponse;
import roomit.main.domain.studyroom.service.StudyRoomService;
import roomit.main.domain.workplace.dto.response.DistanceWorkplaceResponse;
import roomit.main.domain.workplace.service.WorkplaceService;

@RestController
@RequestMapping("/api/v1/studyroom")
@RequiredArgsConstructor
@Slf4j
public class StudyRoomController {

    private final StudyRoomService studyRoomService;
    private final WorkplaceService workplaceService;

    //스터디룸 등록
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{workplaceId}")
    public Map<String, Long> createStudyRoom(@PathVariable @Positive Long workplaceId,
                                @RequestBody CreateStudyRoomRequest request,
                                @AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        Map<String, Long> response = new HashMap<>();
        response.put("studyroomId", studyRoomService.createStudyRoom(workplaceId,request, customBusinessDetails.getId()));
        return response;
    }

    // 사업장의 스터디룸 찾기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/workplace/{workplaceId}")
    public List<StudyRoomListResponse> findStudyRoomsByWorkplace(@PathVariable @Positive Long workplaceId) {
        return studyRoomService.findStudyRoomsByWorkPlaceId(workplaceId);
    }

    // 스터디룸 수정
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{studyRoomId}")
    public void updateStudyRoom(@PathVariable @Positive Long studyRoomId,
                                @RequestBody @Valid UpdateStudyRoomRequest request,
                                @AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        studyRoomService.updateStudyRoom(studyRoomId,request,customBusinessDetails.getId());
    }

    // 스터디룸 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{studyRoomId}")
    public void deleteStudyRoom(@PathVariable @Positive Long studyRoomId,
                                @AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        studyRoomService.deleteStudyRoom(studyRoomId, customBusinessDetails.getId());
    }

    // 스터디룸 상세 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{studyRoomId}")
    public StudyRoomResponse getStudyRoom(@PathVariable @Positive Long studyRoomId) {
        return studyRoomService.getStudyRoom(studyRoomId);
    }

    //스터디룸 예약 가능한 시간대 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search/{studyRoomId}")
    public ReservationPossibleStudyRoomResponse searchPossibleTime(@PathVariable @Positive Long studyRoomId,
                                                                   @RequestParam(required = false) LocalDate checkDate) {
        return studyRoomService.getPossibleReservation(studyRoomId, checkDate);
    }

    // 검색 필터링
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/available")
    public List<FindPossibleStudyRoomResponse> findAvailableStudyRooms(@RequestBody @Valid FindAvailableStudyRoomRequest request) {
        List<DistanceWorkplaceResponse> searchWorkPlace = workplaceService.findNearbyWorkplaces(request.address(), 10000);

        return studyRoomService.findAvailableStudyRooms(request, searchWorkPlace);
    }
}