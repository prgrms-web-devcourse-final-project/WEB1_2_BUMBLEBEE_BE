package roomit.web1_2_bumblebee_be.global.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomit.web1_2_bumblebee_be.global.error.ErrorResponse;
import roomit.web1_2_bumblebee_be.global.exception.CommonException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> commonException(CommonException e) {
        String statusCode = e.getErrorCode().getCode();
        ErrorResponse response = ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .build();

        return ResponseEntity.status(Integer.parseInt(statusCode)).body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse.ErrorValidation> exceptionHandler(MethodArgumentNotValidException e) {

        ErrorResponse.ErrorValidation response = ErrorResponse.ErrorValidation.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();
        for(FieldError fieldError : e.getFieldErrors()){
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(400).body(response);

    }
}
