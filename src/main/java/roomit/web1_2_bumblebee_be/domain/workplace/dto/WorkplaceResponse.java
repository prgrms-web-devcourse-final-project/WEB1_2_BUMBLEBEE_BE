package roomit.web1_2_bumblebee_be.domain.workplace.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class WorkplaceResponse {

    private Long businessId;

    private String workplaceName;
    private String workplacePhoneNumber;
    private String workplaceDescription;
    private String workplaceAddress;

    private byte[] profileImage;
    private String imageType;
    private LocalDateTime workplaceStartTime;
    private LocalDateTime workplaceEndTime;
    private LocalDateTime createdAt;

    public WorkplaceResponse(Workplace workplace) {
//        this.businessId = workplace.getBusiness().getBusinessId();
        this.workplaceName = workplace.getWorkplaceName();
        this.workplacePhoneNumber = workplace.getWorkplacePhoneNumber();
        this.workplaceDescription = workplace.getWorkplaceDescription();
        this.workplaceAddress = workplace.getWorkplaceAddress();
        this.profileImage = workplace.getProfileImage();
        this.imageType = workplace.getImageType();
        this.workplaceStartTime = workplace.getWorkplaceStartTime();
        this.workplaceEndTime = workplace.getWorkplaceEndTime();
        this.createdAt = workplace.getCreatedAt();
    }
}
