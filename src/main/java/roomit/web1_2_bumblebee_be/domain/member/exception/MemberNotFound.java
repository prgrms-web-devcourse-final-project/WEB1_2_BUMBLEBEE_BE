package roomit.web1_2_bumblebee_be.domain.member.exception;

public class MemberNotFound extends commonException{

    private static final String MESSAGE = "존재 하지 않는 회원입니다.";

    public MemberNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
