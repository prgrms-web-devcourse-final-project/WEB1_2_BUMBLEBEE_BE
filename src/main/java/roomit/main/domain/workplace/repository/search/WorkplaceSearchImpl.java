package roomit.main.domain.workplace.repository.search;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import roomit.main.domain.workplace.dto.response.DistanceWorkplaceResponse;
import roomit.main.domain.workplace.entity.QWorkplace;

@Repository
@Slf4j
public class WorkplaceSearchImpl implements WorkplaceSearch  {

  private final JPAQueryFactory queryFactory;

  public WorkplaceSearchImpl(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  @Override
  public List<DistanceWorkplaceResponse> findNearbyWorkplaces(Double longitude, Double latitude, Double maxDistance) {

    QWorkplace workplace = QWorkplace.workplace;

    String location = String.format("POINT(%f %f)", longitude, latitude);

    log.info("pointWKT: {}", location);

    return queryFactory.select(
            Projections.constructor(DistanceWorkplaceResponse.class,
                workplace.workplaceId,
                numberTemplate(Double.class,
                    "ST_Distance(ST_Transform({0}, 5181), ST_Transform(ST_GeomFromText({1}, 5181), 5181))", // 5181 좌표계 사용
                    workplace.location, location)
            )
        )
        .from(workplace)
        .where(
            numberTemplate(Double.class,
                "ST_Distance(ST_Transform({0}, 5181), ST_Transform(ST_GeomFromText({1}, 5181), 5181))", // 5181 좌표계 사용
                workplace.location, location)
                .loe(maxDistance)
        )
        .orderBy(
            numberTemplate(Double.class,
                "ST_Distance(ST_Transform({0}, 5181), ST_Transform(ST_GeomFromText({1}, 5181), 5181))", // 5181 좌표계 사용
                workplace.location, location)
                .asc()
        )
        .fetch();
  }
}
