package roomit.main.domain.studyroom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.main.domain.studyroom.dto.request.FindAvailableStudyRoomRequest;
import roomit.main.domain.studyroom.dto.request.UpdateStudyRoomRequest;
import roomit.main.domain.studyroom.dto.response.FindPossibleStudyRoomResponse;
import roomit.main.domain.studyroom.dto.response.RecentStudyRoomResponse;
import roomit.main.domain.studyroom.dto.response.StudyRoomResponse;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.service.StudyRoomService;

import java.util.List;
import java.util.Optional;

@RestController

@RequiredArgsConstructor
public class StudyRoomController {

    private final StudyRoomService studyRoomService;


    //스터디룸 만들기 v
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/studyroom")
    public void createStudyRoom(@RequestBody CreateStudyRoomRequest request) {
        studyRoomService.createStudyRoom(request);
    }

    // 사업장으로 스터디룸 찾기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/studyroom/workplace/{workplaceId}")
    public void findStudyRoomsByWorkplace(@PathVariable Long workplaceId) {
        List<StudyRoomResponse> studyRooms = studyRoomService.findStudyRoomsByWorkPlaceId(workplaceId);
    }

    // 모든 스터디룸 찾기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/studyroom")
    public void getAllStudyRooms() {
        List<StudyRoom> studyRooms = studyRoomService.getAllStudyRooms();
    }

    // 스터디룸 단건 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/studyroom/{studyRoomId}")
    public void getStudyRoom(@PathVariable Long studyRoomId) {
        StudyRoom studyRoom = studyRoomService.getStudyRoom(studyRoomId);
    }

    // 스터디룸 업데이트
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/api/v1/studyroom")
    public void updateStudyRoom(@RequestBody UpdateStudyRoomRequest request) {
        studyRoomService.updateStudyRoom(request);
    }

    // 스터디룸 정보 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/studyroom/{studyRoomId}")
    public void deleteStudyRoom(@PathVariable Long studyRoomId) {
        studyRoomService.deleteStudyRoom(studyRoomId);
    }


    // 예약가능한 스터디룸 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/studyroom/available")
    public List<FindPossibleStudyRoomResponse> findAvailableStudyRooms(FindAvailableStudyRoomRequest request) {
        return studyRoomService.findAvailableStudyRooms(request);
    }
}