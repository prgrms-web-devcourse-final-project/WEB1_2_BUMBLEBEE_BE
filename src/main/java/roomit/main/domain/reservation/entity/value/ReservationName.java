package roomit.main.domain.reservation.entity.value;

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
public class ReservationName {
    public static final String REGEX = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$";
    public static final String ERR_MSG = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "ReservationName", nullable = false, length = 10)
    private String value;

    public ReservationName(final String name) {
        if (!PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = name;
    }
}
