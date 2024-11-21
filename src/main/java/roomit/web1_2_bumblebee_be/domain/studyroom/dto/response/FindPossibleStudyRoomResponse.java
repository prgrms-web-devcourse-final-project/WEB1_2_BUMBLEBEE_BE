package roomit.web1_2_bumblebee_be.domain.studyroom.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class FindPossibleStudyRoomResponse {
    private String workplaceName;
    private String workplaceAddress;
    private String studyRoomTitle;
    private Integer studyRoomCapacity;
    private Integer studyRoomPrice;
    private Double averageReviewScore;
    private String imageUrl;


    @Builder
    public FindPossibleStudyRoomResponse(String workplaceName, String workplaceAddress, String studyRoomTitle,
                                         Integer studyRoomCapacity, Integer studyRoomPrice, Double averageReviewScore,String imageUrl) {
        this.workplaceName = workplaceName;
        this.workplaceAddress = workplaceAddress;
        this.studyRoomTitle = studyRoomTitle;
        this.studyRoomCapacity = studyRoomCapacity;
        this.studyRoomPrice = studyRoomPrice;
        this.averageReviewScore = averageReviewScore;
        this.imageUrl = imageUrl;
    }
}