package roomit.main.domain.studyroom.entity.value;

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
public class StudyRoomName {
    public static final String REGEX = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-\\s~!@#$%^&*()_+\\[\\]{}|;:',.<>?/]{1,20}$";
    public static final String ERR_MSG = "스터디룸명은 1~20자리여야 합니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "StudyRoom_name", nullable = false, length = 20)
    private String value;

    public StudyRoomName(final String name) {
        if (!PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = name;
    }
}

