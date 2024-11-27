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
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileUploadController {


    private final FileUploadService fileUploadService;

    @GetMapping("/api/generate-presigned-url")
    public ResponseEntity<Map<String, Object>> generatePresignedUrl(@RequestParam String extension) {
        String url = fileUploadService.generatePreSignUrl(UUID.randomUUID() + "." + extension, HttpMethod.PUT);
        Map<String, Object> response = new HashMap<>();
        response.put("presignedUrl", url);
        response.put("method", "PUT");
        response.put("headers", Map.of("Content-Type", "image/" + extension));
        return ResponseEntity.ok(response);
    }


}