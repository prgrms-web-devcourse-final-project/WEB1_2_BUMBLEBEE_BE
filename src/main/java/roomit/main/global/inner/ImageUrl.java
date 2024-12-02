package roomit.main.global.inner;

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
    // 이미지 URL 검증을 위한 정규식
    public static final String REGEX = "^[a-zA-Z0-9가-힣\\s/.-]+$";
    public static final String ERR_MSG = "영문, 숫자, 한글, 공백, /, ., -만 포함해야 합니다.";
    public static final String DB_REGEX = "^https://[a-zA-Z0-9.-]+\\.s3\\.[a-zA-Z0-9.-]+\\.amazonaws\\.com(/[a-zA-Z0-9가-힣\\s/.-]+)*$";
    public static final String DB_ERR_MSG = "이미지 URL은 https://로 시작하며, 올바른 S3 URL 형식이어야 합니다.";
    private static final Pattern PATTERN = Pattern.compile(DB_REGEX);

    // S3 URL의 기본 부분 (버킷 이름과 리전 포함)
    private static final String S3_BASE_URL = "https://{bucket-name}.s3.{region}.amazonaws.com/";

    @Column(name = "image_url", nullable = true, length = 255)
    private String value;

    // 생성자에서 S3 버킷 URL과 리전 값을 받도록 수정
    public ImageUrl(final String imageUrl, final String s3BucketUrl, final String s3Region) {
        if (imageUrl == null) {
            throw new IllegalArgumentException("이미지 URL이 null일 수 없습니다.");
        }

        // s3BucketUrl 및 s3Region이 null인지 확인
        if (s3BucketUrl == null || s3Region == null) {
            throw new IllegalArgumentException("S3 버킷 이름 또는 리전 값이 설정되지 않았습니다.");
        }

        // S3 URL을 생성 (리전과 버킷 이름을 포함한 URL)
        String s3Url = S3_BASE_URL.replace("{bucket-name}", s3BucketUrl)
            .replace("{region}", s3Region) + imageUrl;

        // URL 검증
        if (!PATTERN.matcher(s3Url).matches()) {
            throw new IllegalArgumentException(DB_ERR_MSG);
        }

        // 유효한 경우 value에 S3 URL 저장
        this.value = s3Url;
    }
}
