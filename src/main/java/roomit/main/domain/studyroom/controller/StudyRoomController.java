package roomit.main.domain.studyroom.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.main.domain.studyroom.dto.request.FindAvailableStudyRoomRequest;
import roomit.main.domain.studyroom.dto.request.UpdateStudyRoomRequest;
import roomit.main.domain.studyroom.dto.response.FindPossibleStudyRoomResponse;
import roomit.main.domain.studyroom.dto.response.StudyRoomResponse;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.service.StudyRoomService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyRoomController {

    private final StudyRoomService studyRoomService;


    //스터디룸 만들기 v
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/studyroom/{workPlaceId}")
    public void createStudyRoom(@PathVariable @Positive Long workPlaceId,@RequestBody CreateStudyRoomRequest request) {
        studyRoomService.createStudyRoom(workPlaceId,request);
    }

    // 사업장으로 스터디룸 찾기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/studyroom/workplace/{workplaceId}")
    public List<StudyRoomResponse> findStudyRoomsByWorkplace(@PathVariable @Positive Long workplaceId) {
        return studyRoomService.findStudyRoomsByWorkPlaceId(workplaceId);
    }

    // 스터디룸 단건 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/studyroom/{studyRoomId}")
    public StudyRoom getStudyRoom(@PathVariable @Positive Long studyRoomId) {
        return studyRoomService.getStudyRoom(studyRoomId);
    }

    // 스터디룸 업데이트
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/api/v1/studyroom/{studyRoomId}")
    public void updateStudyRoom(@PathVariable Long studyRoomId,@RequestBody @Valid UpdateStudyRoomRequest request) {
        studyRoomService.updateStudyRoom(studyRoomId,request);
    }

    // 스터디룸 정보 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/studyroom/{studyRoomId}")
    public void deleteStudyRoom(@PathVariable @Positive Long studyRoomId) {
        studyRoomService.deleteStudyRoom(studyRoomId);
    }


    // 예약가능한 스터디룸 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/studyroom/available")
    public List<FindPossibleStudyRoomResponse> findAvailableStudyRooms(@RequestBody @Valid FindAvailableStudyRoomRequest request) {
        return studyRoomService.findAvailableStudyRooms(request);
    }
}