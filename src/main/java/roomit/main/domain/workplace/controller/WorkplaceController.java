package roomit.main.domain.workplace.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.workplace.dto.request.WorkplaceGetRequest;
import roomit.main.domain.workplace.dto.request.WorkplaceRequest;
import roomit.main.domain.workplace.dto.response.WorkplaceAllResponse;
import roomit.main.domain.workplace.dto.response.WorkplaceBusinessResponse;
import roomit.main.domain.workplace.dto.response.WorkplaceCreateResponse;
import roomit.main.domain.workplace.dto.response.WorkplaceDetailResponse;
import roomit.main.domain.workplace.service.WorkplaceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workplace")
public class WorkplaceController {

    private final WorkplaceService workplaceService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/distance")
    public List<WorkplaceAllResponse> getWorkplaces(@RequestBody WorkplaceGetRequest request) {
        return workplaceService.readAllWorkplaces(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/info/{workplaceId}")
    public WorkplaceDetailResponse getWorkplace(@PathVariable Long workplaceId) {
        return workplaceService.readWorkplace(workplaceId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public WorkplaceCreateResponse create(@Valid @RequestBody WorkplaceRequest workplaceDto,
                        @AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        return workplaceService.createWorkplace(workplaceDto, customBusinessDetails.getId());
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
