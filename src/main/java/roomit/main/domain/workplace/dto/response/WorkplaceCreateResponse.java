package roomit.main.domain.workplace.dto.response;

import java.util.List;

public record WorkplaceCreateResponse(
    Long workplaceId,
    List<Long> studyroomId
) {
  public WorkplaceCreateResponse (Long workplaceId, List<Long> studyroomId){
    this.workplaceId = workplaceId;
    this.studyroomId = studyroomId;
  }
}

