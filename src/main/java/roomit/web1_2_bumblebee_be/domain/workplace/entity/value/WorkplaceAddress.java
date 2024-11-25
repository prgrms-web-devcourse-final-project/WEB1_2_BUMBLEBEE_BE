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
    public static final String REGEX = "^[a-zA-Z가-힣0-9\\s(),-]{5,100}$";
    public static final String ERR_MSG = "입력은 5자 이상 100자 이하의 영문, 한글, 숫자, 공백, 괄호(), 쉼표, 하이픈(-)만 허용됩니다.";
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
