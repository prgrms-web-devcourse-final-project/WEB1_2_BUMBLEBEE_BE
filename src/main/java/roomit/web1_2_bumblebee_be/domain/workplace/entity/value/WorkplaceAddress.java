package roomit.web1_2_bumblebee_be.domain.workplace.entity.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

import static lombok.AccessLevel.*;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
public class WorkplaceAddress {
    public static final String REGEX = "^[ㄱ-ㅎ가-힣a-zA-Z0-9\\s,.-]{5,100}$";
    public static final String ERR_MSG = "주소는 5~100자 이내로 입력해야 하며, 특수문자는 ',.-'만 허용됩니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "workplace_address", nullable = false, length = 100)
    private String value;

    public WorkplaceAddress(final String address) {
        if (!PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = address;
    }
}
