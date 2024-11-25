package roomit.main.domain.business.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import roomit.main.domain.business.entity.value.BusinessEmail;
import roomit.main.domain.business.entity.value.BusinessNickname;
import roomit.main.domain.business.entity.value.BusinessNum;

@Builder
public record BusinessUpdateRequest (
    @Pattern(regexp = BusinessNickname.REGEX, message = BusinessNickname.ERR_MSG) String businessName,
    @Pattern(regexp = BusinessEmail.REGEX, message = BusinessEmail.ERR_MSG) String businessEmail,
    @Pattern(regexp = BusinessNum.REGEX, message = BusinessNum.ERR_MSG) String businessNum
){

}
