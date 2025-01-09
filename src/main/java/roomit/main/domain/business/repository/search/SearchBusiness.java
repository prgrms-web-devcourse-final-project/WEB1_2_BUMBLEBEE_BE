package roomit.main.domain.business.repository.search;

import roomit.main.domain.business.dto.request.BusinessUpdateRequest;

public interface SearchBusiness {
    void updateBusiness(BusinessUpdateRequest updateRequest, Long businessId);
}
