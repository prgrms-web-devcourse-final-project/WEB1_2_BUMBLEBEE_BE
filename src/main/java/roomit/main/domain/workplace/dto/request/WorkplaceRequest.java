package roomit.main.domain.workplace.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import org.locationtech.jts.geom.Point;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.studyroom.dto.request.CreateStudyRoomRequest;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.WorkplaceAddress;
import roomit.main.domain.workplace.entity.value.WorkplaceName;
import roomit.main.domain.workplace.entity.value.WorkplacePhoneNumber;

@Builder
public record WorkplaceRequest(
        @Pattern(regexp = WorkplaceName.REGEX, message = WorkplaceName.ERR_MSG) String workplaceName,
        @Pattern(regexp = WorkplacePhoneNumber.REGEX, message = WorkplacePhoneNumber.ERR_MSG) String workplacePhoneNumber,
        @NotBlank(message = "사업장 세부사항을 입력해주세요") String workplaceDescription,
        @Pattern(regexp = WorkplaceAddress.REGEX, message = WorkplaceAddress.ERR_MSG) String workplaceAddress,
        @NotNull(message = "사업장 시작 시간을 입력해주세요.") @JsonFormat(pattern = "HH:mm") LocalTime workplaceStartTime,
        @NotNull(message = "사업장 종료 시간을 입력해주세요.") @JsonFormat(pattern = "HH:mm") LocalTime workplaceEndTime,
        List<CreateStudyRoomRequest> studyRoomList
) {
    public Workplace toEntity(Point location, Business business) {

        return Workplace.builder()
                .workplaceName(workplaceName)
                .workplaceDescription(workplaceDescription)
                .workplacePhoneNumber(workplacePhoneNumber)
                .workplaceAddress(workplaceAddress)
                .workplaceStartTime(workplaceStartTime)
                .workplaceEndTime(workplaceEndTime)
                .location(location)
                .business(business)
                .build();
    }
}
