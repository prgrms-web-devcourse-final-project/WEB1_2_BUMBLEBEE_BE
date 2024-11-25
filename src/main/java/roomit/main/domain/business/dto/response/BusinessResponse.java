package roomit.main.domain.business.dto.response;

import roomit.main.domain.business.entity.Business;
import java.time.LocalDateTime;


public record BusinessResponse (
    String businessName,
    String businessEmail,
    String businessNum,
    LocalDateTime createAt
){

    public BusinessResponse(Business business) {
        this(
            business.getBusinessName(),
            business.getBusinessEmail(),
            business.getBusinessNum(),
            business.getCreatedAt()
        );
    }

}
