package roomit.web1_2_bumblebee_be.domain.business.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import roomit.web1_2_bumblebee_be.domain.business.entity.Business;
import roomit.web1_2_bumblebee_be.domain.business.entity.value.BusinessEmail;
import roomit.web1_2_bumblebee_be.domain.business.entity.value.BusinessNickname;
import roomit.web1_2_bumblebee_be.domain.business.entity.value.BusinessNum;
import roomit.web1_2_bumblebee_be.domain.business.entity.value.BusinessPassword;


@Getter
@Builder
public class BusinessRegisterRequest {

    @Pattern(regexp = BusinessNickname.REGEX, message = BusinessNickname.ERR_MSG)
    private final String businessName;

    @Pattern(regexp = BusinessEmail.REGEX, message = BusinessEmail.ERR_MSG)
    private final String businessEmail;

    @Pattern(regexp = BusinessPassword.REGEX, message = BusinessPassword.ERR_MSG)
    private final String businessPwd;

    @Pattern(regexp = BusinessNum.REGEX, message = BusinessNum.ERR_MSG)
    private final String businessNum;

    public Business toEntity(final PasswordEncoder passwordEncoder) {
        return Business.builder()
                .businessName(businessName)
                .businessEmail(businessEmail)
                .businessPwd(businessPwd)
                .businessNum(businessNum)
                .passwordEncoder(passwordEncoder)
                .build();
    }
}
