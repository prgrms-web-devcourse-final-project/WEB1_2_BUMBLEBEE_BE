package roomit.main.global.oauth2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Oauth2Controller {

    @GetMapping("/noauth")
    public ResponseEntity<?> noAuth() {
        Map<String, String> body = new HashMap<>();
        body.put("message", "unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
}
