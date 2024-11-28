package roomit.main.domain.workplace.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record WorkplaceGetResponse(
        Long workplaceId,
        String workplaceName,
        String workplacePhoneNumber,
        String workplaceDescription,
        String workplaceAddress,
        String imageUrl,
        @JsonFormat(pattern = "HH:mm") LocalTime workplaceStartTime,
        @JsonFormat(pattern = "HH:mm") LocalTime workplaceEndTime,
        LocalDateTime createdAt,
        BigDecimal longitude,
        BigDecimal latitude,
        Double distance
) {}

