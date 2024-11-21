package roomit.web1_2_bumblebee_be.domain.workplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomit.web1_2_bumblebee_be.domain.business.entity.value.BusinessNickname;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.ImageUrl;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplaceAddress;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplaceName;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplacePhoneNumber;

import java.time.LocalDateTime;

@Getter
@Builder
public class WorkplaceRequest {

    @Pattern(regexp = WorkplaceName.REGEX, message = WorkplaceName.ERR_MSG)
    private String workplaceName;

    @Pattern(regexp = WorkplacePhoneNumber.REGEX, message = WorkplacePhoneNumber.ERR_MSG)
    private String workplacePhoneNumber;

    @NotBlank(message = "사업장 세부사항을 입력해주세요")
    private String workplaceDescription;

    @Pattern(regexp = WorkplaceAddress.REGEX, message = WorkplaceAddress.ERR_MSG)
    private String workplaceAddress;

    @Pattern(regexp = ImageUrl.REGEX, message = ImageUrl.ERR_MSG)
    private String imageUrl;

    @NotNull(message = "사업장 시작 시간을 입력해주세요.")
    private LocalDateTime workplaceStartTime;

    @NotNull(message = "사업장 종료 시간을 입력해주세요.")
    private LocalDateTime workplaceEndTime;

    public Workplace toEntity() {
        return Workplace.builder()
                .workplaceName(workplaceName)
                .workplaceDescription(workplaceDescription)
                .workplacePhoneNumber(workplacePhoneNumber)
                .workplaceAddress(workplaceAddress)
                .imageUrl(imageUrl)
                .workplaceStartTime(workplaceStartTime)
                .workplaceEndTime(workplaceEndTime)
                .build();
    }
}
