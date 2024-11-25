package roomit.web1_2_bumblebee_be.domain.oauth2.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomit.web1_2_bumblebee_be.domain.member.dto.CustomMemberDetails;

@RestController
@RequestMapping("/oauth")
@Log4j2
public class MyController {

    @GetMapping("/my")
    public String mainAPI(){

        return "my route";
    }

    @GetMapping("/user")
    public String userAPI(@AuthenticationPrincipal CustomMemberDetails principal){
        log.info("principal.getName()"+principal.getName());
        log.info("principal.getUsername()"+principal.getUsername());
        log.info("principal.getAttributes()"+principal.getAttributes());
        log.info("principal.getId()"+principal.getId());

        return "user";
    }

    @GetMapping("business")
    public String businessAPI(){

        return "business";
    }
}
