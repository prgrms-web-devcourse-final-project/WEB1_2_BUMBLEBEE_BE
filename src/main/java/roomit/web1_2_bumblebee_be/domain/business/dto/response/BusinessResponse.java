package roomit.web1_2_bumblebee_be.domain.business.dto.response;

import roomit.web1_2_bumblebee_be.domain.business.entity.Business;
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
