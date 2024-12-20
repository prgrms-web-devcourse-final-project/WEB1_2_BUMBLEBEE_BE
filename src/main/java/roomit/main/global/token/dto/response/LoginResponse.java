package roomit.main.global.token.dto.response;

import roomit.main.domain.member.entity.Role;

public record LoginResponse(
        Role role
) {
}
