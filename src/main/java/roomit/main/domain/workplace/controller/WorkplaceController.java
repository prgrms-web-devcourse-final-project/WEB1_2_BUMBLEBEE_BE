package roomit.main.domain.workplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.workplace.dto.request.WorkplaceGetRequest;
import roomit.main.domain.workplace.dto.request.WorkplaceRequest;
import roomit.main.domain.workplace.dto.response.WorkplaceAllResponse;
import roomit.main.domain.workplace.dto.response.WorkplaceBusinessResponse;
import roomit.main.domain.workplace.dto.response.WorkplaceDetailResponse;
import roomit.main.domain.workplace.service.WorkplaceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workplace")
public class WorkplaceController {

    private final WorkplaceService workplaceService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<WorkplaceAllResponse> getWorkplaces(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestBody WorkplaceGetRequest request) {
        return workplaceService.readAllWorkplaces(request, latitude, longitude);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/info/{workplaceId}")
    public WorkplaceDetailResponse getWorkplace(@PathVariable Long workplaceId) {
        return workplaceService.readWorkplace(workplaceId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public void create(@Valid @RequestBody WorkplaceRequest workplaceDto,
                        @AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        workplaceService.createWorkplace(workplaceDto, customBusinessDetails.getId());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{workplaceId}")
    public void update(@PathVariable Long workplaceId, @Valid @RequestBody WorkplaceRequest workplaceDto,
                       @AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        workplaceService.updateWorkplace(workplaceId, workplaceDto, customBusinessDetails.getId());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{workplaceId}")
    public void delete(@PathVariable Long workplaceId,
                       @AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        workplaceService.deleteWorkplace(workplaceId, customBusinessDetails.getId());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/business") // 사업자ID로 사업장 조회
    public WorkplaceBusinessResponse getWorkplacesByBusinessId(@AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        System.out.println(customBusinessDetails);
        return workplaceService.findWorkplacesByBusinessId(customBusinessDetails.getId());
    }
}
