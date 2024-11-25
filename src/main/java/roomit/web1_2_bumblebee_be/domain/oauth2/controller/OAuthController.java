package roomit.web1_2_bumblebee_be.domain.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuthController {
    @GetMapping("/login")
    public String home() {
        return "login";
    }

    @GetMapping("/private")
    public String privatePage() {
        return "privatePage";
    }
    @GetMapping("/admin")
    public String adminPage() {
        return "adminPage";
    }
}