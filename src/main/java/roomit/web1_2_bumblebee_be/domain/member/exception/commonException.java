package roomit.web1_2_bumblebee_be.domain.member.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class commonException extends RuntimeException{

    public commonException(String message) {
        super(message);
    }

    public abstract int getStatusCode();

}
