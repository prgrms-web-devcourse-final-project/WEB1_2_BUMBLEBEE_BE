package roomit.web1_2_bumblebee_be.domain.workplace.exception;

public class WorkplaceTaskException extends RuntimeException{
    private final int statusCode;

    public WorkplaceTaskException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
