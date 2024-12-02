package roomit.main.domain.reservation.entity.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.regex.Pattern;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationNum {
    public static final String REGEX = "^[0-9]{3}-[0-9]{4}-[0-9]{4}";
    public static final String ERR_MSG = "예약자 전화번호는 10자리 번호로 이루어져야합니다";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "ReservationNum", nullable = false, length = 30)
    private String value;

    public ReservationNum(final String nickname) {
        if (!PATTERN.matcher(nickname).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = nickname;
    }
}
