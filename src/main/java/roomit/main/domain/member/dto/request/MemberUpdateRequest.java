package roomit.main.domain.member.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import roomit.main.domain.member.entity.value.MemberEmail;
import roomit.main.domain.member.entity.value.MemberNickname;
import roomit.main.domain.member.entity.value.MemberPassword;
import roomit.main.domain.member.entity.value.MemberPhoneNumber;


public record MemberUpdateRequest(
        @Pattern(regexp = MemberNickname.REGEX, message = MemberNickname.ERR_MSG)
        String nickName,
        @Pattern(regexp = MemberPhoneNumber.REGEX, message = MemberPhoneNumber.ERR_MSG)
        String phoneNumber,
        @Pattern(regexp = MemberEmail.REGEX, message = MemberEmail.ERR_MSG)
        String email,
        @Pattern(regexp = MemberPassword.REGEX, message = MemberPassword.ERR_MSG)
        String pwd
) {
    @Builder
    public MemberUpdateRequest {
    }
}
