package roomit.web1_2_bumblebee_be.domain.studyroom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.FindPossibleStudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.UpdateStudyRoomRequest;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.response.FindPossibleStudyRoomResponse;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;
import roomit.web1_2_bumblebee_be.domain.studyroom.service.StudyRoomService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyRoomController {
    private final StudyRoomService studyRoomService;

    // 스터디룸 생성
    @PostMapping
    public ResponseEntity<Void> createStudyRoom(@RequestBody CreateStudyRoomRequest request) {
        studyRoomService.createStudyRoom(request);
        return ResponseEntity.ok().build();  // 200 OK 반환
    }

    // 스터디룸 조회 (전체)
    @GetMapping
    public ResponseEntity<List<StudyRoom>> getAllStudyRooms() {
        List<StudyRoom> studyRooms = studyRoomService.getAllStudyRooms();
        return ResponseEntity.ok(studyRooms);
    }

    // ID로 특정 스터디룸 조회
    @GetMapping("/{id}")
    public ResponseEntity<StudyRoom> getStudyRoomById(@PathVariable Long id) {
        StudyRoom studyRoom = studyRoomService.getStudyRoom(id);
        return ResponseEntity.ok(studyRoom);
    }

    // 스터디룸 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStudyRoom( @RequestBody UpdateStudyRoomRequest request) {
        request = new UpdateStudyRoomRequest(request.getStudyRoomId(), request.getTitle(), request.getDescription(), request.getCapacity(), request.getPrice());
        studyRoomService.updateStudyRoom(request);
        return ResponseEntity.noContent().build();
    }

    // 스터디룸 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudyRoom(@PathVariable Long id) {
        studyRoomService.deleteStudyRoom(id);
        return ResponseEntity.noContent().build();
    }

    // 사용 가능한 스터디룸 목록 조회
    @PostMapping("/available")
    public ResponseEntity<List<FindPossibleStudyRoomResponse>> findAvailableStudyRooms(@RequestBody FindPossibleStudyRoomRequest request) {
        List<FindPossibleStudyRoomResponse> availableStudyRooms = studyRoomService.findAvailableStudyRooms(request);
        return ResponseEntity.ok(availableStudyRooms);
    }

}

