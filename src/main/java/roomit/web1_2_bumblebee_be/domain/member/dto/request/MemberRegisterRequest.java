package roomit.web1_2_bumblebee_be.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.member.entity.Age;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;

@Getter
public class MemberRegisterRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickName;

    @NotBlank(message = "휴대번호를 입력해주세요.")
    private String phoneNumber;

    @NotNull(message = "나이대를 입력해주세요")
    private Age age;

    @NotNull(message = "성별을 입력해주세요.")
    private Sex sex;

    @NotNull(message = "이메일을 입력해주세요.")
    private String email;

    @NotNull(message = "비밀번호를 입력해주세요.")
    private String pwd;

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
