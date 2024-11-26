package roomit.main.domain.workplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.business.dto.CustomBusinessDetails;
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
    public List<WorkplaceResponse> getWorkplaces() {
        List<WorkplaceResponse> workplaceList = workplaceService.readAllWorkplaces();
        return workplaceList;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/workplace/{workplaceId}")
    public WorkplaceResponse getWorkplace(@PathVariable Long workplaceId) {
        WorkplaceResponse workplace = workplaceService.readWorkplace(workplaceId);
        return workplace;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/workplace")
    public void create(@Valid @RequestBody WorkplaceRequest workplaceDto,
                        @AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        workplaceService.createWorkplace(workplaceDto, customBusinessDetails.getId());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/api/v1/workplace/{workplaceId}")
    public void update(@PathVariable Long workplaceId, @Valid @RequestBody WorkplaceRequest workplaceDto,
                       @AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        workplaceService.updateWorkplace(workplaceId, workplaceDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/workplace/{workplaceId}")
    public void delete(@PathVariable Long workplaceId, @AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        workplaceService.deleteWorkplace(workplaceId);
    }
//
//    @GetMapping("/api/v1/business/workplace") // 사업자ID로 사업장 조회
//    public ResponseEntity<List<WorkplaceResponse>> getWorkplacesByBusinessId(@AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
//        List<WorkplaceResponse> workplaces = workplaceService.findWorkplacesByBusinessId(customBusinessDetails.getId());
//        return ResponseEntity.ok(workplaces);
//    }
}
