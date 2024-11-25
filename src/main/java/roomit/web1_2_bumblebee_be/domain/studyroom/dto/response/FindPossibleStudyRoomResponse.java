package roomit.web1_2_bumblebee_be.domain.studyroom.dto.response;

import roomit.web1_2_bumblebee_be.domain.review.entity.Review;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.ImageUrl;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplaceAddress;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplaceName;

public record FindPossibleStudyRoomResponse(
    WorkplaceName workplaceName,
    Double averageReviewScore,
    String studyRoomTitle,
    double reviewScore,
    WorkplaceAddress workplaceAddress,
    Integer studyRoomCapacity,
    Integer studyRoomPrice,
    ImageUrl imageUrl){


    public static FindPossibleStudyRoomResponse from(Workplace workplace, StudyRoom studyRoom, Review review){
        return new FindPossibleStudyRoomResponse(
                workplace.getWorkplaceName(),
                review.getReviewRating(),
                studyRoom.getTitle(),
                review.getReviewRating(),
                workplace.getWorkplaceAddress(),
                studyRoom.getCapacity(),
                studyRoom.getPrice(),
                workplace.getImageUrl()
        );
    }

}
