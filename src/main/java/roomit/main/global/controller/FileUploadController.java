package roomit.main.global.controller;

import com.amazonaws.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.global.service.FileUploadService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileUploadController {


    private final FileUploadService awsS3Service;

    @Value("${amazon.aws.bucket}")
    private String bucketName;

    @GetMapping("/api/generate-presigned-url")
    public ResponseEntity<String> generatePresignedUrl(@RequestParam String extension){
        return ResponseEntity.ok(
                awsS3Service.generatePreSignUrl(UUID.randomUUID()+"."+extension,bucketName, HttpMethod.PUT));

    }


}