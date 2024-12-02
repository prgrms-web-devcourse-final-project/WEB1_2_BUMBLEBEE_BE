package roomit.main.domain.studyroom.dto.response;

import roomit.main.domain.review.entity.Review;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.ImageUrl;
import roomit.main.domain.workplace.entity.value.WorkplaceAddress;
import roomit.main.domain.workplace.entity.value.WorkplaceName;

public record FindPossibleStudyRoomResponse(
    WorkplaceName workplaceName,
    String studyRoomTitle,
    int reviewScore,
    WorkplaceAddress workplaceAddress,
    Integer studyRoomCapacity,
    Integer studyRoomPrice,
    ImageUrl imageUrl){


    public static FindPossibleStudyRoomResponse from(Workplace workplace, StudyRoom studyRoom, Review review){
        return new FindPossibleStudyRoomResponse(
                workplace.getWorkplaceName(),
                studyRoom.getTitle(),
                review.getReviewRating(),
                workplace.getWorkplaceAddress(),
                studyRoom.getCapacity(),
                studyRoom.getPrice(),
                workplace.getImageUrl()
        );
    }

}
