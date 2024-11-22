package roomit.web1_2_bumblebee_be.domain.member.dto.response;

import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.member.entity.Age;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;

import java.time.LocalDateTime;

@Getter
public class MemberResponse {

    private final String nickName;

    private final String phoneNumber;

    private final Age age;

    private final Sex sex;

    private final String email;

    private final String pwd;

    private final Role role;

    private LocalDateTime createAt;

    public MemberResponse(Member member) {
        this.nickName = member.getMemberNickName();
        this.phoneNumber = member.getMemberPhoneNumber();
        this.email = member.getMemberEmail();
        this.pwd = member.getMemberPwd();
        this.age = member.getMemberAge();
        this.sex = member.getMemberSex();
        this.role = member.getMemberRole();
        this.createAt = LocalDateTime.now();
    }

}
