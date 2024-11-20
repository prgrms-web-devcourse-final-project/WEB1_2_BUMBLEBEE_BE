package roomit.web1_2_bumblebee_be.domain.member.response;

import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.member.entity.Age;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;

import java.time.LocalDateTime;

@Getter
public class MemberResponse {
    private Long id;

    private String nickName;

    private String phoneNumber;

    private Age age;

    private Sex sex;

    private String email;

    private String pwd;

    private Role role;

    private LocalDateTime createAt;

    public MemberResponse(Member member) {
        this.id = member.getMemberId();
        this.nickName = member.getMemberNickName();
        this.phoneNumber = member.getMemberPhoneNumber();
        this.age = member.getMemberAge();
        this.sex = member.getMemberSex();
        this.email = member.getMemberEmail();
        this.pwd = member.getMemberPwd();
        this.role = member.getMemberRole();
        this.createAt = LocalDateTime.now();
    }

}
