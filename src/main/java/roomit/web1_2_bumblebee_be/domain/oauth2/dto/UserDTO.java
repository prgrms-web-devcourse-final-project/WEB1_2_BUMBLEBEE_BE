package roomit.web1_2_bumblebee_be.domain.oauth2.dto;

import lombok.Getter;
import lombok.Setter;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;

@Getter
@Setter
public class UserDTO {

    private Role role;

    private String name;

    private String username;

    private String email;

}
