package roomit.main.domain.workplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        return workplaceService.readAllWorkplaces();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/workplace/{workplaceId}")
    public WorkplaceResponse getWorkplace(@PathVariable Long workplaceId) {
        return workplaceService.readWorkplace(workplaceId);
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/business/workplace") // 사업자ID로 사업장 조회
    public List<WorkplaceResponse> getWorkplacesByBusinessId(@AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        return workplaceService.findWorkplacesByBusinessId(customBusinessDetails.getId());
    }
}
