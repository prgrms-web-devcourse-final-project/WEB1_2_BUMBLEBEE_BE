package roomit.main.global.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class FileUploadService {

    private final S3Presigner s3Presigner;

    @Value("${amazon.aws.bucket}")
    private String bucketName;

    public FileUploadService(@Value("${amazon.aws.accessKey}") String accessKey,
                             @Value("${amazon.aws.secretKey}") String secretKey,
                             @Value("${amazon.aws.region}") String region) {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    public Map<String, Object> generatePreSignUrl(String fileName, String extension) {
        String filePath = fileName + "/" + UUID.randomUUID() + "." + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(10))
                .build();

        String url = s3Presigner.presignPutObject(presignRequest).url().toString();

        // URL 정보 반환
        Map<String, Object> response = new HashMap<>();
        response.put("presignedUrl", url);
        response.put("method", "PUT");
        response.put("headers", Map.of("Content-Type", "image/" + extension));
        response.put("filePath", filePath);

        return response;
    }
}