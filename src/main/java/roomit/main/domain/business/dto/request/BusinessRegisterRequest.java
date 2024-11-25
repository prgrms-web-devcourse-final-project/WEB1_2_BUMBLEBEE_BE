package roomit.main.domain.business.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.business.entity.value.BusinessEmail;
import roomit.main.domain.business.entity.value.BusinessNickname;
import roomit.main.domain.business.entity.value.BusinessNum;
import roomit.main.domain.business.entity.value.BusinessPassword;


@Builder
public record BusinessRegisterRequest(@Pattern(regexp = BusinessNickname.REGEX, message = BusinessNickname.ERR_MSG) String businessName,
                                      @Pattern(regexp = BusinessEmail.REGEX, message = BusinessEmail.ERR_MSG) String businessEmail,
                                      @Pattern(regexp = BusinessPassword.REGEX, message = BusinessPassword.ERR_MSG) String businessPwd,
                                      @Pattern(regexp = BusinessNum.REGEX, message = BusinessNum.ERR_MSG) String businessNum) {

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
