package roomit.web1_2_bumblebee_be.domain.workplace.exception;

import roomit.web1_2_bumblebee_be.global.error.ErrorCode;
import roomit.web1_2_bumblebee_be.global.exception.commonException;

public class WorkplaceInvalidRequest extends commonException {
    public WorkplaceInvalidRequest() {
        super(ErrorCode.WORKPLACE_INVALID_REQUEST);
    }
}