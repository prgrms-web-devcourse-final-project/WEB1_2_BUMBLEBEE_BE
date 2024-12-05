package roomit.main.domain.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import roomit.main.domain.reservation.entity.value.ReservationName;
import roomit.main.domain.reservation.entity.value.ReservationNum;

public record UpdateReservationRequest (

    @Pattern(regexp = ReservationName.REGEX, message = ReservationName.ERR_MSG) String reservationName,

    @Pattern(regexp = ReservationNum.REGEX, message = ReservationNum.ERR_MSG) String reservationPhoneNumber,

    @NotNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,

    @NotNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
){
}