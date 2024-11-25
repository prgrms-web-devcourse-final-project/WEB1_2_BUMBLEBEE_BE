package roomit.web1_2_bumblebee_be.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;


public record ErrorResponse (ErrorCode errorCode,Map<String, String> validation){

    @Builder
    public ErrorResponse {
    }

        public record ErrorValidation(String code, String message, Map<String, String> validation) {

            @Builder
            public ErrorValidation(String code, String message, Map<String, String> validation) {
                this.code = code;
                this.message = message;
                this.validation = validation != null ? validation : new HashMap<>();
            }

            public void addValidation(String fieldName, String errorMessage) {
                this.validation.put(fieldName, errorMessage);
            }
        }
}