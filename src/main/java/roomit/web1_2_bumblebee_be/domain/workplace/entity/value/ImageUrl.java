package roomit.web1_2_bumblebee_be.domain.workplace.entity.value;

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
public class ImageUrl {
    public static final String REGEX = "^(http|https)://.*$";
    public static final String ERR_MSG = "이미지 URL은 http 또는 https로 시작해야 합니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(name = "image_url", nullable = true, length = 255)
    private String value;

    public ImageUrl(final String imageUrl) {
        if (imageUrl != null && !PATTERN.matcher(imageUrl).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = imageUrl;
    }
}