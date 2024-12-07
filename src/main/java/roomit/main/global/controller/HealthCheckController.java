package roomit.main.global.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

  @GetMapping("/health")
  @ResponseStatus(HttpStatus.OK)
  public String healthCheck() {
    return "OK"; // ELB가 200 응답을 기대하므로 간단한 응답
  }

  @GetMapping("/")
  @ResponseStatus(HttpStatus.OK)
  public String rootCheck() {
    return "OK"; // 루트 경로도 헬스 체크로 사용 가능
  }
}
