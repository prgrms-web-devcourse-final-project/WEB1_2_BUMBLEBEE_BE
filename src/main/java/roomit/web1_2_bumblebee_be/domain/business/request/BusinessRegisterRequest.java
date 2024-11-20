package roomit.web1_2_bumblebee_be.domain.business.request;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class BusinessRegisterRequest {

    private final String businessNum;

    private final String businessName;

    private final String businessEmail;

    private final String businessPwd;

}
