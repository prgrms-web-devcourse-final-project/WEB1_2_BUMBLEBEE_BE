package roomit.web1_2_bumblebee_be.domain.business.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.business.entity.value.BusinessEmail;
import roomit.web1_2_bumblebee_be.domain.business.entity.value.BusinessNickname;
import roomit.web1_2_bumblebee_be.domain.business.entity.value.BusinessNum;

@Getter
@Builder
public class BusinessUpdateRequest {

    @Pattern(regexp = BusinessNickname.REGEX, message = BusinessNickname.ERR_MSG)
    private final String businessName;

    @Pattern(regexp = BusinessEmail.REGEX, message = BusinessEmail.ERR_MSG)
    private final String businessEmail;

    @Pattern(regexp = BusinessNum.REGEX, message = BusinessNum.ERR_MSG)
    private final String businessNum;

}
