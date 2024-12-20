package roomit.main.domain.studyroom.repository.search;

import java.util.List;
import roomit.main.domain.studyroom.entity.StudyRoom;

public interface SearchStudyRoom {
  List<StudyRoom> findByWorkPlaceId(List<Long> workplaceIds, Integer reservationCapacity);
}
