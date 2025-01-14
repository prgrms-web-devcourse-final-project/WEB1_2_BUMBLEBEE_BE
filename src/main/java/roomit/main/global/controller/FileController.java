package roomit.main.global.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.global.service.FileLocationService;
import roomit.main.global.service.FileUploadService;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileUploadService fileUploadService;
    private final FileLocationService fileLocationService;

    @GetMapping("/api/generate-presigned-url")
    public ResponseEntity<Object> generatePresignedUrls(@RequestParam(required = false) String fileName,
                                                        @RequestParam(required = false) String fileLocation) {

        // 단일 URL 생성
        if(fileName != null && fileLocation != null) {
            Map<String, Object> singleUrl = fileUploadService.generaㅣtePreSignUrl(fileName, fileLocation);
            return ResponseEntity.ok(singleUrl);
        }

        // 요청이 올바르지 않은 경우
        return ResponseEntity.badRequest().body("Both 'fileName' and 'fileLocation' cannot be null.");
    }


    @DeleteMapping("/api/delete-folder")
    public void deleteFolder(@RequestParam(required = false) String fileLocation) {
        fileLocationService.deleteImageFromFolder(fileLocation);
    }
}