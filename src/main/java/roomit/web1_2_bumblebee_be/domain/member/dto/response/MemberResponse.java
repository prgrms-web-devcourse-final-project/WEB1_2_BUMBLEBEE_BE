package roomit.web1_2_bumblebee_be.domain.member.dto.response;

import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;

import java.time.LocalDateTime;

@Getter
public class MemberResponse {
    private Long id;

    private String nickName;

    private String phoneNumber;

    private int age;

    private Sex sex;

    private String email;

    private String pwd;

    private Role role;

    private LocalDateTime createAt;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.nickName = member.getNickName();
        this.phoneNumber = member.getPhoneNumber();
        this.age = member.getAge();
        this.sex = member.getSex();
        this.email = member.getEmail();
        this.pwd = member.getPwd();
        this.role = member.getRole();
        this.createAt = LocalDateTime.now();
    }

}
