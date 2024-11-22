package roomit.web1_2_bumblebee_be.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.value.MemberEmail;
import roomit.web1_2_bumblebee_be.domain.member.entity.value.MemberNickname;
import roomit.web1_2_bumblebee_be.domain.member.entity.value.MemberPassword;
import roomit.web1_2_bumblebee_be.domain.member.entity.value.MemberPhoneNumber;

@Getter
public class MemberUpdateRequest {

    @Pattern(regexp = MemberNickname.REGEX, message = MemberNickname.ERR_MSG)
    private final String nickName;

    @Pattern(regexp = MemberPhoneNumber.REGEX, message = MemberPhoneNumber.ERR_MSG)
    private final String phoneNumber;

    @Pattern(regexp = MemberEmail.REGEX, message = MemberEmail.ERR_MSG)
    private final String email;

    @Pattern(regexp = MemberPassword.REGEX, message = MemberPassword.ERR_MSG)
    private final String pwd;

    @Builder
    public MemberUpdateRequest( String email, String phoneNumber, String pwd, String memberNickName) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pwd = pwd;
        this.nickName = memberNickName;
    }
}
