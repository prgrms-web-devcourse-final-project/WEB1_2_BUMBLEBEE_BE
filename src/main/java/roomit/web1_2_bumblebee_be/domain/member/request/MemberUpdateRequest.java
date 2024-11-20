package roomit.web1_2_bumblebee_be.domain.member.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;

@Getter
public class MemberUpdateRequest {

    @NotNull(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "휴대번호를 입력해주세요.")
    private String phoneNumber;
    @NotNull(message = "비밀번호를 입력해주세요.")
    private String pwd;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String memberNickName;

    @Builder
    public MemberUpdateRequest( String email, String phoneNumber, String pwd, String memberNickName) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pwd = pwd;
        this.memberNickName = memberNickName;
    }
}
