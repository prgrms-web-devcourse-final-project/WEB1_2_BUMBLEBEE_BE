package roomit.main.global.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.global.service.FileUploadService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @GetMapping("/api/generate-presigned-url")
    public ResponseEntity<Object> generatePresignedUrls(
            @RequestParam(required = false) String extension,  // 단일 확장자
            @RequestParam(required = false) List<String> extensions) { // 다수 확장자

        // 단일 URL 생성
        if (extension != null) {
            Map<String, Object> singleUrl = fileUploadService.generatePreSignUrl(extension);
            return ResponseEntity.ok(singleUrl);
        }

        // 다수 URL 생성
        if (extensions != null && !extensions.isEmpty()) {
            List<Map<String, Object>> multipleUrls = fileUploadService.generatePreSignUrls(extensions);
            return ResponseEntity.ok(multipleUrls);
        }

        // 요청이 올바르지 않은 경우
        return ResponseEntity.badRequest().body("Either 'extension' or 'extensions' must be provided.");
    }
}