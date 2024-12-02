package roomit.main.global.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.global.service.FileUploadService;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @GetMapping("/api/generate-presigned-url")
    public ResponseEntity<Object> generatePresignedUrls(
            @RequestParam(required = false) String extension,  // 단일 확장자
            @RequestParam(required = false) String fileName) { // 다수 확장자

        // 단일 URL 생성
        if (extension != null) {
            Map<String, Object> singleUrl = fileUploadService.generatePreSignUrl(fileName,extension);
            return ResponseEntity.ok(singleUrl);
        }

        // 요청이 올바르지 않은 경우
        return ResponseEntity.badRequest().body("Either 'extension' or 'extensions' must be provided.");
    }
}