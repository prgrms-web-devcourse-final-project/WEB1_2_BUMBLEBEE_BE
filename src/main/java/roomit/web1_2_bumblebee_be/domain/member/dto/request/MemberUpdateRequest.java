package roomit.web1_2_bumblebee_be.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;

@Getter
public class MemberUpdateRequest {
    private int age;
    private String email;
    private Role role;
    private String phoneNumber;
    private String pwd;

    @Builder
    public MemberUpdateRequest(int age, String email, Role role, String phoneNumber, String pwd) {
        this.age = age;
        this.email = email;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.pwd = pwd;
    }
}
