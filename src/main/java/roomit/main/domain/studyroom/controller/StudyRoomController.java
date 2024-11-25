package roomit.main.domain.studyroom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.main.domain.studyroom.dto.request.UpdateStudyRoomRequest;
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


    //스터디룸 만들기
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/studyroom")
    public StudyRoomResponse createStudyRoom(@RequestBody CreateStudyRoomRequest request) {
        return studyRoomService.createStudyRoom(request);
    }

    // 사업장으로 스터디룸 찾기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/studyroom/{workplaceId}")
    public void findStudyRoomsByWorkplace(@PathVariable Long workplaceId) {
        List<StudyRoom> studyRooms = studyRoomService.findStudyRoomsByWorkplace(workplaceId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/studyroom")
    public void getAllStudyRooms() {
        List<StudyRoom> studyRooms = studyRoomService.getAllStudyRooms();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/studyroom/{studyRoomId}")
    public void getStudyRoom(@PathVariable Long studyRoomId) {
        StudyRoom studyRoom = studyRoomService.getStudyRoom(studyRoomId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/api/v1/studyroom")
    public void updateStudyRoom(@RequestBody UpdateStudyRoomRequest request) {
        studyRoomService.updateStudyRoom(request);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/studyroom/{studyRoomId}")
    public void deleteStudyRoom(@PathVariable Long studyRoomId) {
        studyRoomService.deleteStudyRoom(studyRoomId);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/studyroom/recent/{memberId}")
    public Optional<RecentStudyRoomResponse> findRecentReservation(@PathVariable Long memberId) {
        return studyRoomService.findRecentReservation(memberId);
    }

//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping("/api/v1/studyroom/available")
//    public List<FindPossibleStudyRoomResponse> findAvailableStudyRooms(FindAvailableStudyRoomRequest request) {
//        return studyRoomService.findAvailableStudyRooms(request);
//    }
}