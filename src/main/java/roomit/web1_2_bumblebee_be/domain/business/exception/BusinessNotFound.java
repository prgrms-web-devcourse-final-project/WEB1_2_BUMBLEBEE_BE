package roomit.web1_2_bumblebee_be.domain.business.exception;

import roomit.web1_2_bumblebee_be.domain.member.exception.commonException;

public class BusinessNotFound extends commonException {

    private static final String MESSAGE = "존재 하지 않는 사업자입니다.";

    public BusinessNotFound() {super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 404;
    }
}
