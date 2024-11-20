package roomit.web1_2_bumblebee_be.domain.business.exception;

import roomit.web1_2_bumblebee_be.global.error.ErrorCode;
import roomit.web1_2_bumblebee_be.global.exception.commonException;

public class BusinessNotModify extends commonException {

    public BusinessNotModify() {super(ErrorCode.BUSINESS_NOT_MODIFY);}

}
