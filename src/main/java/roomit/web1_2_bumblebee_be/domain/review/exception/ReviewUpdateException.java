package roomit.web1_2_bumblebee_be.domain.review.exception;

import roomit.web1_2_bumblebee_be.global.error.ErrorCode;
import roomit.web1_2_bumblebee_be.global.exception.commonException;

public class ReviewUpdateException extends commonException {

    public ReviewUpdateException() {
        super(ErrorCode.REVIEW_UPDATE_EXCEPTION);
    }
}
