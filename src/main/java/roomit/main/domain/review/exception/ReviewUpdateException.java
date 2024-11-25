package roomit.main.domain.review.exception;

import roomit.main.global.error.ErrorCode;
import roomit.main.global.exception.CommonException;

public class ReviewUpdateException extends CommonException {

    public ReviewUpdateException() {
        super(ErrorCode.REVIEW_UPDATE_EXCEPTION);
    }
}
