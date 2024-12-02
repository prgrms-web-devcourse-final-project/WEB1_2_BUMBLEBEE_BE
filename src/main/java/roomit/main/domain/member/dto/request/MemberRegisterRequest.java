package roomit.main.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.entity.Sex;
import roomit.main.domain.member.entity.value.MemberEmail;
import roomit.main.domain.member.entity.value.MemberNickname;
import roomit.main.domain.member.entity.value.MemberPassword;
import roomit.main.domain.member.entity.value.MemberPhoneNumber;
import roomit.main.global.oauth2.dto.PROVIDER;

import java.time.LocalDate;

@Builder
public record MemberRegisterRequest(@Pattern(regexp = MemberNickname.REGEX, message = MemberNickname.ERR_MSG) String nickName,
                                    @Pattern(regexp = MemberPhoneNumber.REGEX, message = MemberPhoneNumber.ERR_MSG) String phoneNumber,
                                    @Pattern(regexp = MemberEmail.REGEX, message = MemberEmail.ERR_MSG) String email,
                                    @Pattern(regexp = MemberPassword.REGEX, message = MemberPassword.ERR_MSG) String pwd,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDay,
                                    @NotNull(message = "성별 입력해주세요") Sex sex) {


    public Member toEntity(final PasswordEncoder passwordEncoder){
        return   Member.builder()
                .birthDay(birthDay)
                .memberSex(sex)
                .memberPwd(pwd) //암호화
                .memberEmail(email)
                .memberPhoneNumber(phoneNumber)
                .memberNickName(nickName)
                .provider(PROVIDER.BASIC)
                .passwordEncoder(passwordEncoder)
                .build();
    }

}
