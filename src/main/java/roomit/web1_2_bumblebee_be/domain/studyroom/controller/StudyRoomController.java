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
    @PostMapping("/api/v1/studyroom/create")
    public ResponseEntity<String> createStudyRoom(@RequestBody CreateStudyRoomRequest request) {
        studyRoomService.createStudyRoom(request);
        return ResponseEntity.status(201).body("스터디룸 생성이 성공적으로 완료되었습니다.");  // 201 OK 반환
    }

    // 스터디룸 조회 (전체)
    @GetMapping("/api/v1/studyroom/list")
    public ResponseEntity<List<StudyRoom>> getAllStudyRooms() {
        List<StudyRoom> studyRooms = studyRoomService.getAllStudyRooms();
        return ResponseEntity.ok(studyRooms);
    }

    // ID로 특정 스터디룸 조회
    @GetMapping("/api/v1/studyroom/{id}")
    public ResponseEntity<StudyRoom> getStudyRoomById(@PathVariable Long id) {
        StudyRoom studyRoom = studyRoomService.getStudyRoom(id);
        return ResponseEntity.ok(studyRoom);
    }

    // 스터디룸 업데이트
    @PutMapping("/api/v1/studyroom/{id}")
    public ResponseEntity<String> updateStudyRoom( @RequestBody UpdateStudyRoomRequest request) {
        request = new UpdateStudyRoomRequest(request.getStudyRoomId(), request.getTitle(), request.getDescription(), request.getCapacity(), request.getPrice());
        studyRoomService.updateStudyRoom(request);
        return ResponseEntity.status(200).body("스터디룸 수정이 성공적으로 완료되었습니다.");
    }

    // 스터디룸 삭제
    @DeleteMapping("/api/v1/studyroom/{id}")
    public ResponseEntity<String> deleteStudyRoom(@PathVariable Long id) {
        studyRoomService.deleteStudyRoom(id);
        return ResponseEntity.status(200).body("스터디룸 삭제가 성공적으로 완료되었습니다.");
    }

    // 사업장별 스터디룸 목록 조회
    @GetMapping("/api/v1/studyroom/workplace/{workplaceId}")
    public ResponseEntity<List<StudyRoom>> findStudyRoomsByWorkPlaceId(@PathVariable Long workplaceId) {
        List<StudyRoom> studyRooms = studyRoomService.findStudyRoomsByWorkPlaceId(workplaceId);
        return ResponseEntity.ok(studyRooms);
    }

    // 사용 가능한 스터디룸 목록 조회
    @PostMapping("/api/v1/studyroom/available")
    public ResponseEntity<List<FindPossibleStudyRoomResponse>> findAvailableStudyRooms(@RequestBody FindPossibleStudyRoomRequest request) {
        List<FindPossibleStudyRoomResponse> availableStudyRooms = studyRoomService.findAvailableStudyRooms(request);
        return ResponseEntity.ok(availableStudyRooms);
    }

}