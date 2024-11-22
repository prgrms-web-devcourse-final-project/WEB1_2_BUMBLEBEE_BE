package roomit.web1_2_bumblebee_be.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.member.entity.Age;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;
import roomit.web1_2_bumblebee_be.domain.member.entity.value.MemberEmail;
import roomit.web1_2_bumblebee_be.domain.member.entity.value.MemberNickname;
import roomit.web1_2_bumblebee_be.domain.member.entity.value.MemberPassword;
import roomit.web1_2_bumblebee_be.domain.member.entity.value.MemberPhoneNumber;

@Getter
public class MemberRegisterRequest {

    @Pattern(regexp = MemberNickname.REGEX, message = MemberNickname.ERR_MSG)
    private final String nickName;

    @Pattern(regexp = MemberPhoneNumber.REGEX, message = MemberPhoneNumber.ERR_MSG)
    private final String phoneNumber;

    @Pattern(regexp = MemberEmail.REGEX, message = MemberEmail.ERR_MSG)
    private final String email;

    @Pattern(regexp = MemberPassword.REGEX, message = MemberPassword.ERR_MSG)
    private final String pwd;


    @NotNull(message = "나이 입력해주세요")
    private final Age age;

    @NotNull(message = "성별 입력해주세요")
    private final Sex sex;

    @Builder
    public MemberRegisterRequest(String nickName, String phoneNumber, Age age, Sex sex, String email, String pwd, Role role) {
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.sex = sex;
        this.email = email;
        this.pwd = pwd;
    }

}
