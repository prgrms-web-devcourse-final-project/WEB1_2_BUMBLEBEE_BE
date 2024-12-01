package roomit.main.global.error;

import lombok.Builder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public record ErrorResponse (ErrorCode errorCode,Map<String, String> validation){
    @Builder
    public ErrorResponse {
    }

        public record ErrorValidation(String code, String message, Map<String, String> validation) {

            @Builder
            public ErrorValidation(String code, String message, Map<String, String> validation) {
                this.code = code;
                this.message = message;
                this.validation = Objects.requireNonNullElseGet(validation, HashMap::new);
            }

            public void addValidation(String fieldName, String errorMessage) {
                this.validation.put(fieldName, errorMessage);
            }
        }
}