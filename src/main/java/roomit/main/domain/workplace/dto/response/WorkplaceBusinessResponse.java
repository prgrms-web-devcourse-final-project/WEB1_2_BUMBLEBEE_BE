package roomit.main.domain.workplace.dto.response;

import java.util.List;

public record WorkplaceBusinessResponse(
        Long businessId,
        String businessName,
        List<WorkplaceResponse> workplaces
) {}
