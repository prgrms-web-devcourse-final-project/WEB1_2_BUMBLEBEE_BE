package roomit.web1_2_bumblebee_be.domain.workplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceRequest;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceResponse;
import roomit.web1_2_bumblebee_be.domain.workplace.service.WorkplaceService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WorkplaceController {

    private final WorkplaceService workplaceService;

    @GetMapping("/api/v1/workplace")
    public ResponseEntity<List<WorkplaceResponse>> getWorkplaces() {
        List<WorkplaceResponse> workplaceList = workplaceService.readAllWorkplaces();
        return ResponseEntity.ok().body(workplaceList);
    }

    @GetMapping("/api/v1/workplace/{workplaceId}")
    public ResponseEntity<WorkplaceResponse> getWorkplace(@PathVariable Long workplaceId) {
        WorkplaceResponse workplace = workplaceService.readWorkplace(workplaceId);
        return ResponseEntity.ok().body(workplace);
    }

    @PostMapping("/api/v1/workplace/create")
    public ResponseEntity<?> create(@Valid @RequestBody WorkplaceRequest workplaceDto) {
        workplaceService.createWorkplace(workplaceDto);
        return ResponseEntity.status(201).body(Map.of("message", "workplace created"));
    }

    @PutMapping("/api/v1/workplace/{workplaceId}")
    public ResponseEntity<?> update(@PathVariable Long workplaceId, @Valid @RequestBody WorkplaceRequest workplaceDto) {
        workplaceService.updateWorkplace(workplaceId, workplaceDto);
        return ResponseEntity.ok().body(Map.of("message", "workplace updated"));
    }

    @DeleteMapping("/api/v1/workplace/{workplaceId}")
    public ResponseEntity<?> delete(@PathVariable Long workplaceId) {
        workplaceService.deleteWorkplace(workplaceId);
        return ResponseEntity.status(204).body(Map.of("message", "workplace deleted"));
    }

//    @GetMapping("/api/v1/business/workplace") // 사업자ID로 사업장 조회
//    public ResponseEntity<List<WorkplaceResponse>> getWorkplacesByBusinessId(/*@AuthenticationPrincipal CustomUserDetails customUser*/) {
//        List<WorkplaceResponse> workplaces = workplaceService.findWorkplacesByBusinessId(customUser.getBusinessId());
//        return ResponseEntity.ok(workplaces);
//    }
}
