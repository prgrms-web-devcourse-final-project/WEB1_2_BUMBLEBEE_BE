package roomit.main.global.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomit.main.global.error.ErrorCode;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
public class FileLocationService {

  private final S3Client s3Client;

  public FileLocationService(@Value("${amazon.aws.accessKey}") String accessKey,
                             @Value("${amazon.aws.secretKey}") String secretKey,
                             @Value("${amazon.aws.region}") String region) {
    // 자격 증명 및 리전 정보를 사용하여 S3Client 초기화
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

    this.s3Client = S3Client.builder()
        .region(Region.of(region)) // 프로퍼티에서 리전 정보 설정
        .credentialsProvider(StaticCredentialsProvider.create(credentials)) // 자격 증명 제공
        .build();
  }

  @Value("${amazon.aws.bucket}")
  private String bucketName;


  public List<String> getImagesFromFolder(String folderPath) {
    List<String> imageUrls = new ArrayList<>();

    String baseUrl = "https://elasticbeanstalk-ap-northeast-2-405894845535.s3.ap-northeast-2.amazonaws.com/";

    try {
      String path = folderPath.substring(baseUrl.length());  // baseUrl 길이만큼 잘라냄

      // S3 객체 목록 요청
      ListObjectsV2Request request = ListObjectsV2Request.builder()
          .bucket(bucketName)
          .prefix(path + "/")  // 폴더 경로 지정
          .build();

      ListObjectsV2Response response;
      do {
        response = s3Client.listObjectsV2(request);


        // 리스트가 비어있지 않다면
        if (response.contents() != null && !response.contents().isEmpty()) {
          for (S3Object object : response.contents()) {
            String key = object.key();

            // 이미지 파일인지 확인 (확장자 필터)
            if (key.endsWith(".jpg") || key.endsWith(".png") || key.endsWith(".jpeg")) {
              // 이미지 URL 생성 (bucketName과 region을 추가)
              String imageUrl = "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + key;
              imageUrls.add(imageUrl);
            }
          }
        }
        // 다음 페이지 처리
        request = request.toBuilder()
            .continuationToken(response.nextContinuationToken())
            .build();

      } while (response.isTruncated());

    } catch (Exception e) {
      throw ErrorCode.S3_IMAGE_FETCH_FAILED.commonException();
    }

    if(imageUrls.isEmpty()){
    imageUrls.add("S3 이미지를 불러오는데 실패했습니다.");
    }
    return imageUrls;
  }


  public void deleteImageFromFolder(String folderPath) {
    try {
      // 폴더 내 객체 목록 가져오기
      ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
          .bucket(bucketName)
          .prefix(folderPath)
          .build();

      ListObjectsV2Response listObjectsResponse;
      do {
        listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);

        // 객체 삭제
        listObjectsResponse.contents().forEach(s3Object -> {
          String objectKey = s3Object.key();
          s3Client.deleteObject(DeleteObjectRequest.builder()
              .bucket(bucketName)
              .key(objectKey)
              .build());
        });

        // 다음 페이지 처리
        listObjectsRequest = listObjectsRequest.toBuilder()
            .continuationToken(listObjectsResponse.nextContinuationToken())
            .build();
      } while (listObjectsResponse.isTruncated());


    } catch (Exception e) {
      throw ErrorCode.S3_IMAGE_NOT_DELETE.commonException();
    }
  }

}
