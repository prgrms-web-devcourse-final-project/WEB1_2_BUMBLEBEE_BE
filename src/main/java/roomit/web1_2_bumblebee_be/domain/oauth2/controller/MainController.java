package roomit.web1_2_bumblebee_be.domain.oauth2.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class MainController {

    @GetMapping("/")
    public String mainAPI(){

        return "main route";
    }
}
