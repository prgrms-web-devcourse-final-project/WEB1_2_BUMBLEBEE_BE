package roomit.web1_2_bumblebee_be.domain.member.exception;

import roomit.web1_2_bumblebee_be.global.error.ErrorCode;
import roomit.web1_2_bumblebee_be.global.exception.commonException;

public class MemberUpdateException extends commonException {

    public MemberUpdateException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
