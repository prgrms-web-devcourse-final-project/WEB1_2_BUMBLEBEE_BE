package roomit.main.domain.studyroom.repository.search;

import static roomit.main.domain.studyroom.entity.QStudyRoom.studyRoom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.stereotype.Repository;
import roomit.main.domain.studyroom.entity.StudyRoom;

@Repository
public class SearchStudyRoomImpl implements SearchStudyRoom {

  private final JPAQueryFactory queryFactory;

  public SearchStudyRoomImpl(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  @Override
  public List<StudyRoom> findByWorkPlaceId(List<Long> workplaceIds, Integer reservationCapacity) {

    return queryFactory
        .selectFrom(studyRoom)
        .where(
            studyRoom.workPlace.workplaceId.in(workplaceIds)
                .and(studyRoom.capacity.goe(reservationCapacity))
        )
        .distinct() // 중복 제거
        .fetch();
  }
}
