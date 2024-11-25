package roomit.main.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class ErrorResponse {

    private final String code;
    private final String message;


    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
    }

}