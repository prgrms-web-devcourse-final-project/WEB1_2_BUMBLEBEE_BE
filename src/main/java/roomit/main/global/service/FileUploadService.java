package roomit.main.global.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

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

    public String generatePreSignUrl(String filePath, HttpMethod method) {
        if (method == HttpMethod.PUT) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .putObjectRequest(putObjectRequest)
                    .signatureDuration(Duration.ofMinutes(10))
                    .build();

            return s3Presigner.presignPutObject(presignRequest).url().toString();
        }

        throw new IllegalArgumentException("Unsupported HTTP method");
    }
}