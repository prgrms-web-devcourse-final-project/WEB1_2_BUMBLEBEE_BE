package roomit.main.domain.member.exception;

import roomit.main.global.error.ErrorCode;
import roomit.main.global.exception.CommonException;

public class MemberUpdateException extends CommonException {

    public MemberUpdateException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
