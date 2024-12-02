package roomit.main.domain.workplace.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.global.inner.ImageUrl;
import roomit.main.domain.workplace.entity.value.WorkplaceAddress;
import roomit.main.domain.workplace.entity.value.WorkplaceName;
import roomit.main.domain.workplace.entity.value.WorkplacePhoneNumber;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import roomit.main.global.service.ImageService;

@Builder
public record WorkplaceRequest(
        @Pattern(regexp = WorkplaceName.REGEX, message = WorkplaceName.ERR_MSG) String workplaceName,
        @Pattern(regexp = WorkplacePhoneNumber.REGEX, message = WorkplacePhoneNumber.ERR_MSG) String workplacePhoneNumber,
        @NotBlank(message = "사업장 세부사항을 입력해주세요") String workplaceDescription,
        @Pattern(regexp = WorkplaceAddress.REGEX, message = WorkplaceAddress.ERR_MSG) String workplaceAddress,
        @Pattern(regexp = ImageUrl.REGEX, message = ImageUrl.ERR_MSG) String imageUrl,
        @NotNull(message = "사업장 시작 시간을 입력해주세요.") LocalTime workplaceStartTime,
        @NotNull(message = "사업장 종료 시간을 입력해주세요.") LocalTime workplaceEndTime,
        List<CreateStudyRoomRequest> studyRoomList
) {
    public Workplace toEntity(BigDecimal latitude, BigDecimal longitude, Business business, ImageService imageService) {

        ImageUrl image = imageService.createImageUrl(imageUrl);

        return Workplace.builder()
                .workplaceName(workplaceName)
                .workplaceDescription(workplaceDescription)
                .workplacePhoneNumber(workplacePhoneNumber)
                .workplaceAddress(workplaceAddress)
                .imageUrl(image)
                .workplaceStartTime(workplaceStartTime)
                .workplaceEndTime(workplaceEndTime)
                .latitude(latitude)
                .longitude(longitude)
                .business(business)
                .build();
    }
}
