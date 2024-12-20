package roomit.main.domain.member.entity.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class MemberEmail {
    public static final String REGEX = "^(?=.{1,100}$)[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String ERR_MSG = "이메일 형식이 올바르지 않습니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "member_email", nullable = false, length = 100)
    private String value;

    public MemberEmail(final String email) {
        if (!PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = email;
    }
}
