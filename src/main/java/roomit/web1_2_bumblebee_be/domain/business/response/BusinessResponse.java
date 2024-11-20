package roomit.web1_2_bumblebee_be.domain.business.response;

import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.business.entity.Business;

import java.time.LocalDateTime;

@Getter
public class BusinessResponse {

    private final String businessName;

    private final String businessEmail;

    private final String businessNum;

    private final LocalDateTime createAt;

    public BusinessResponse(Business business) {
        this.businessName = business.getBusinessName();
        this.businessNum = business.getBusinessNum();
        this.businessEmail = business.getBusinessEmail();
        this.createAt = business.getCreatedAt();
    }

}
