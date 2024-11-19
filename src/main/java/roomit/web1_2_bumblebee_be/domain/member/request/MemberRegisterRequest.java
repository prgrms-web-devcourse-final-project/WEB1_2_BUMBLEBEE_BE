package roomit.web1_2_bumblebee_be.domain.member.request;

import lombok.Builder;
import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;

@Getter
public class MemberRegisterRequest {
    private String nickName;

    private String phoneNumber;

    private int age;

    private Sex sex;

    private String email;

    private String pwd;

    @Builder
    public MemberRegisterRequest(String nickName, String phoneNumber, int age, Sex sex, String email, String pwd, Role role) {
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.sex = sex;
        this.email = email;
        this.pwd = pwd;
        this.role = role;
    }

    private Role role;
}
