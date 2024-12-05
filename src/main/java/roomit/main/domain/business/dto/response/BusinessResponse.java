package roomit.main.domain.business.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import roomit.main.domain.business.entity.Business;


public record BusinessResponse (
    String businessName,
    String businessEmail,
    String businessNum,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt
){

    public BusinessResponse(Business business) {
        this(
            business.getBusinessName(),
            business.getBusinessEmail(),
            business.getBusinessNum(),
            business.getCreatedAt()
        );
    }

}
