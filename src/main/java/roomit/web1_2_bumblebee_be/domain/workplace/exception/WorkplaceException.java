package roomit.web1_2_bumblebee_be.domain.workplace.exception;

import org.springframework.http.HttpStatus;

public enum WorkplaceException {
    WORKPLACE_NOT_FOUND("존재하지 않는 사업장 입니다", HttpStatus.NOT_FOUND),
    WORKPLACE_NOT_REGISTERED("사업장 등록에 실패하였습니다.", HttpStatus.UNAUTHORIZED),
    WORKPLACE_NOT_MODIFIED("사업장 수정에 실패하였습니다.", HttpStatus.FORBIDDEN),
    WORKPLACE_NOT_DELETE("사업장 삭제에 실패하였습니다.", HttpStatus.FORBIDDEN),
    WORKPLACE_INVALID_REQUEST("잘못된 입력입니다.", HttpStatus.BAD_REQUEST),
    NOT_UPLOAD_IMAGE("이미지 업로드에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    NOT_REMOVE_IMAGE("이미지를 삭제하는데 실패하였습니다.", HttpStatus.BAD_REQUEST);

    private String message;
    private HttpStatus status;

    WorkplaceException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public WorkplaceTaskException getWorkplaceTaskException() {
        return new WorkplaceTaskException(this.message,this.status.value());
    }

}
