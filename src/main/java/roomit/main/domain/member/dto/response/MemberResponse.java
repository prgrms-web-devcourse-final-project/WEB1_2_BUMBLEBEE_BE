package roomit.main.domain.member.dto.response;

import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.entity.Sex;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record MemberResponse (String nickName,
                              String phoneNumber,
                              LocalDate birthDay,
                              Sex sex,
                              String email,
                              String pwd,
                              LocalDateTime createAt){



    public MemberResponse(Member member) {
        this( member.getMemberNickName(),
        member.getMemberPhoneNumber(),
        member.getBirthDay(),
        member.getMemberSex(),
        member.getMemberEmail(),
        member.getMemberPwd(),
        LocalDateTime.now()
        );
    }

}
