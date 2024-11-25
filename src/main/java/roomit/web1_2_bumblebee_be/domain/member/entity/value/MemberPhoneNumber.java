package roomit.web1_2_bumblebee_be.domain.member.entity.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class MemberPhoneNumber {
    public static final String REGEX = "^\\d{3}-\\d{4}-\\d{4}$";
    public static final String ERR_MSG = "멤버 번호는 10자리 번호로 이루어져야합니다";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "member_phonenumber", nullable = false, length = 30, unique = true)
    private String value;

    public MemberPhoneNumber(final String nickname) {
        if (!PATTERN.matcher(nickname).matches()) {
            log.error(ERR_MSG);
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = nickname;
    }
}
