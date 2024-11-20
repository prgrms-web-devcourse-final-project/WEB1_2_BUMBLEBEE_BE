package roomit.web1_2_bumblebee_be.domain.business.request;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class BusinessRegisterRequest {

    private String businessNum;

    private String businessName;

    private String businessEmail;

    private String businessPwd;

}
