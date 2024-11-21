package roomit.web1_2_bumblebee_be.domain.business.entity.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessNum {
    public static final String REGEX = "^\\d{3}-\\d{2}-\\d{5}$";
    public static final String ERR_MSG = "사업자 번호는 10자리 번호로 이루어져야합니다";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "business_Num", nullable = false, length = 30)
    private String value;

    public BusinessNum(final String nickname) {
        if (!PATTERN.matcher(nickname).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = nickname;
    }
}
