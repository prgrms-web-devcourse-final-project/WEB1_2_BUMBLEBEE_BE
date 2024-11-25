package roomit.main.domain.review.exception;

import roomit.main.global.error.ErrorCode;
import roomit.main.global.exception.CommonException;

public class ReviewNotFound extends CommonException {

    public ReviewNotFound() {
        super(ErrorCode.REVIEW_NOT_FOUND);
    }
}
