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
public class BusinessNickname {
    public static final String REGEX = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$";
    public static final String ERR_MSG = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "business_name", nullable = false, length = 30)
    private String value;

    public BusinessNickname(final String nickname) {
        if (!PATTERN.matcher(nickname).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = nickname;
    }
}
