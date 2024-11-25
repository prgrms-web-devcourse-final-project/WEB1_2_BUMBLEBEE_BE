package roomit.main.domain.business.entity.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.regex.Pattern;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessNum {
    public static final String REGEX = "^[0-9]{3}-[0-9]{2}-[0-9]{5}";
    public static final String ERR_MSG = "사업자 번호는 10자리 번호로 이루어져야합니다";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "business_Num", nullable = false, length = 30, unique = true)
    private String value;

    public BusinessNum(final String nickname) {
        if (!PATTERN.matcher(nickname).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = nickname;
    }
}
