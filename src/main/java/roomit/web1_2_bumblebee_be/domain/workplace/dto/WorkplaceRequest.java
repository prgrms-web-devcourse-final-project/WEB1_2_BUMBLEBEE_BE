package roomit.web1_2_bumblebee_be.domain.workplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkplaceRequest {

    @NotBlank(message = "사업장 이름을 입력해주세요.")
    private String workplaceName;

    @NotBlank(message = "사업장 전화번호를 입력해주세요.")
    private String workplacePhoneNumber;

    @NotBlank(message = "사업장 세부사항을 입력해주세요")
    private String workplaceDescription;

    @NotBlank(message = "사업장 주소를 입력해주세요.")
    private String workplaceAddress;

    private byte[] profileImage;
    private String imageType;

    @NotNull(message = "사업장 시작 시간을 입력해주세요.")
    private LocalDateTime workplaceStartTime;

    @NotNull(message = "사업장 종료 시간을 입력해주세요.")
    private LocalDateTime workplaceEndTime;

    @Builder
    public WorkplaceRequest(Workplace workplace) {
        this.workplaceName = workplace.getWorkplaceName();
        this.workplacePhoneNumber = workplace.getWorkplacePhoneNumber();
        this.workplaceDescription = workplace.getWorkplaceDescription();
        this.workplaceAddress = workplace.getWorkplaceAddress();
        this.profileImage = workplace.getProfileImage();
        this.imageType = workplace.getImageType();
        this.workplaceStartTime = workplace.getWorkplaceStartTime();
        this.workplaceEndTime = workplace.getWorkplaceEndTime();
    }

    public Workplace toEntity() {
        return Workplace.builder()
                .workplaceName(workplaceName)
                .workplacePhoneNumber(workplacePhoneNumber)
                .workplaceDescription(workplaceDescription)
                .workplaceAddress(workplaceAddress)
                .profileImage(profileImage)
                .imageType(imageType)
                .workplaceStartTime(workplaceStartTime)
                .workplaceEndTime(workplaceEndTime)
                .build();
    }


}
