package roomit.main.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import roomit.main.domain.member.entity.Sex;
import roomit.main.domain.member.entity.value.MemberEmail;
import roomit.main.domain.member.entity.value.MemberNickname;
import roomit.main.domain.member.entity.value.MemberPhoneNumber;

import java.time.LocalDate;


public record MemberUpdateRequest(
        @Pattern(regexp = MemberNickname.REGEX, message = MemberNickname.ERR_MSG)
        String nickName,
        @Pattern(regexp = MemberPhoneNumber.REGEX, message = MemberPhoneNumber.ERR_MSG)
        String phoneNumber,
        @Pattern(regexp = MemberEmail.REGEX, message = MemberEmail.ERR_MSG)
        String email,
        @NotNull(message = "생년월일은 필수 입력 값입니다.")
        LocalDate birthDay,
        @NotNull(message = "성별은 필수 입력 값입니다.")
        Sex sex
) {
    @Builder
    public MemberUpdateRequest {
    }
}
