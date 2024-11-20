package roomit.web1_2_bumblebee_be.domain.workplace.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;

import java.time.LocalDateTime;

@Getter
public class WorkplaceResponse {

    private final Long businessId;

    private final String workplaceName;
    private final String workplacePhoneNumber;
    private final String workplaceDescription;
    private final String workplaceAddress;

    private final byte[] profileImage;
    private final String imageType;
    private final LocalDateTime workplaceStartTime;
    private final LocalDateTime workplaceEndTime;
    private final LocalDateTime createdAt;

    public WorkplaceResponse(Workplace workplace) {
        this.businessId = workplace.getBusiness().getBusinessId();
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
