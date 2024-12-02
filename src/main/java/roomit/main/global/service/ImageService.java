package roomit.main.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomit.main.global.inner.ImageUrl;
import roomit.main.global.inner.AwsProperties;

@Service
public class ImageService {

  private final AwsProperties awsProperties;

  @Autowired
  public ImageService(AwsProperties awsProperties) {
    this.awsProperties = awsProperties;
  }

  public ImageUrl createImageUrl(String imageUrl) {
    // AwsProperties에서 S3 설정 값을 가져와서 ImageUrl 객체 생성
    return new ImageUrl(imageUrl, awsProperties.getBucket(), awsProperties.getRegion());
  }
}

