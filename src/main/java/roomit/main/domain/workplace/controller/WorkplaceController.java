package roomit.main.domain.workplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.workplace.dto.WorkplaceRequest;
import roomit.main.domain.workplace.dto.WorkplaceResponse;
import roomit.main.domain.workplace.service.WorkplaceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WorkplaceController {

    private final WorkplaceService workplaceService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/workplace")
    public ResponseEntity<List<WorkplaceResponse>> getWorkplaces() {
        List<WorkplaceResponse> workplaceList = workplaceService.readAllWorkplaces();
        return ResponseEntity.ok().body(workplaceList);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/workplace/{workplaceId}")
    public ResponseEntity<WorkplaceResponse> getWorkplace(@PathVariable Long workplaceId) {
        WorkplaceResponse workplace = workplaceService.readWorkplace(workplaceId);
        return ResponseEntity.ok().body(workplace);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/workplace/create")
    public void create(@Valid @RequestBody WorkplaceRequest workplaceDto) {
        workplaceService.createWorkplace(workplaceDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/api/v1/workplace/{workplaceId}")
    public void update(@PathVariable Long workplaceId, @Valid @RequestBody WorkplaceRequest workplaceDto) {
        workplaceService.updateWorkplace(workplaceId, workplaceDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/workplace/{workplaceId}")
    public void delete(@PathVariable Long workplaceId) {
        workplaceService.deleteWorkplace(workplaceId);
    }

//    @GetMapping("/api/v1/business/workplace") // 사업자ID로 사업장 조회
//    public ResponseEntity<List<WorkplaceResponse>> getWorkplacesByBusinessId(/*@AuthenticationPrincipal CustomUserDetails customUser*/) {
//        List<WorkplaceResponse> workplaces = workplaceService.findWorkplacesByBusinessId(customUser.getBusinessId());
//        return ResponseEntity.ok(workplaces);
//    }
}
