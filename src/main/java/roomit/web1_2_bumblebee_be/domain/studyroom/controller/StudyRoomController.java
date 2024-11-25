package roomit.web1_2_bumblebee_be.domain.studyroom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.FindAvailableStudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.UpdateStudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.response.FindPossibleStudyRoomResponse;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.response.RecentStudyRoomResponse;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.response.StudyRoomResponse;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;
import roomit.web1_2_bumblebee_be.domain.studyroom.service.StudyRoomService;

import java.util.List;
import java.util.Optional;

@RestController

@RequiredArgsConstructor
public class StudyRoomController {

    private final StudyRoomService studyRoomService;

    //스터디룸 만들기
    @PostMapping("/api/v1/studyroom")
    public ResponseEntity<StudyRoomResponse> createStudyRoom(@RequestBody CreateStudyRoomRequest request) {
        StudyRoomResponse response = studyRoomService.createStudyRoom(request);
        return ResponseEntity.ok(response);
    }

    // 사업장으로 스터디룸 찾기
    @GetMapping("/api/v1/workplace/{workplaceId}")
    public ResponseEntity<List<StudyRoom>> findStudyRoomsByWorkplace(@PathVariable Long workplaceId) {
        List<StudyRoom> studyRooms = studyRoomService.findStudyRoomsByWorkplace(workplaceId);
        return ResponseEntity.ok(studyRooms);
    }

    @GetMapping("/api/v1/studyroom")
    public ResponseEntity<List<StudyRoom>> getAllStudyRooms() {
        List<StudyRoom> studyRooms = studyRoomService.getAllStudyRooms();
        return ResponseEntity.ok(studyRooms);
    }

    @GetMapping("/api/v1/studyroom/{studyRoomId}")
    public ResponseEntity<StudyRoom> getStudyRoom(@PathVariable Long studyRoomId) {
        StudyRoom studyRoom = studyRoomService.getStudyRoom(studyRoomId);
        return ResponseEntity.ok(studyRoom);
    }

    @PutMapping("/api/v1/studyroom")
    public ResponseEntity<Void> updateStudyRoom(@RequestBody UpdateStudyRoomRequest request) {
        studyRoomService.updateStudyRoom(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/v1/studyroom/{studyRoomId}")
    public ResponseEntity<Void> deleteStudyRoom(@PathVariable Long studyRoomId) {
        studyRoomService.deleteStudyRoom(studyRoomId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/v1/studyroom/recent/{memberId}")
    public ResponseEntity<Optional<RecentStudyRoomResponse>> findRecentReservation(@PathVariable Long memberId) {
        Optional<RecentStudyRoomResponse> response = studyRoomService.findRecentReservation(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/studyroom/available")
    public ResponseEntity<List<FindPossibleStudyRoomResponse>> findAvailableStudyRooms(FindAvailableStudyRoomRequest request) {
        List<FindPossibleStudyRoomResponse> response = studyRoomService.findAvailableStudyRooms(request);
        return ResponseEntity.ok(response);
    }
}