package roomit.main.domain.workplace.entity.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkplaceName {
    public static final String REGEX = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-\\s]{1,20}$";
    public static final String ERR_MSG = "사업장명은 특수문자를 제외한 1~20자리여야 합니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "workplace_name", nullable = false, length = 20)
    private String value;

    public WorkplaceName(final String name) {
        if (!PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = name;
    }
}

