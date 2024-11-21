package roomit.web1_2_bumblebee_be.domain.studyroom.exception;

import roomit.web1_2_bumblebee_be.global.error.ErrorCode;
import roomit.web1_2_bumblebee_be.global.exception.commonException;

public class StudyRoomNotFound extends commonException{
    public StudyRoomNotFound() { super(ErrorCode.STUDYROOM_NOT_FOUND);}
}


