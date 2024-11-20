package roomit.web1_2_bumblebee_be.domain.workplace.exception;

import roomit.web1_2_bumblebee_be.global.error.ErrorCode;
import roomit.web1_2_bumblebee_be.global.exception.commonException;

public class WorkplaceNotFound extends commonException {
    public WorkplaceNotFound() {
        super(ErrorCode.WORKPLACE_NOT_FOUND);
    }
}
