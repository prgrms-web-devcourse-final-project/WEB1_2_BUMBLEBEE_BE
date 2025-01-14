package roomit.main.domain.workplace.repository.search;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;
import roomit.main.domain.workplace.dto.request.WorkplaceRequest;
import roomit.main.domain.workplace.dto.response.DistanceWorkplaceResponse;
import roomit.main.domain.workplace.entity.QWorkplace;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.WorkplaceAddress;
import roomit.main.domain.workplace.entity.value.WorkplaceName;
import roomit.main.domain.workplace.entity.value.WorkplacePhoneNumber;
import roomit.main.domain.workplace.service.GeoService;
import roomit.main.global.error.ErrorCode;
import roomit.main.global.util.PointUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

@Repository
@Slf4j
public class WorkplaceSearchImpl implements WorkplaceSearch {

    private final JPAQueryFactory queryFactory;
    private final WorkplaceDuplicateChecker workplaceDuplicateChecker;
    private final GeoService geoService;
    private final PointUtil pointUtil;

    public WorkplaceSearchImpl(JPAQueryFactory queryFactory, WorkplaceDuplicateChecker workplaceDuplicateChecker, GeoService geoService, PointUtil pointUtil) {
        this.queryFactory = queryFactory;
        this.workplaceDuplicateChecker = workplaceDuplicateChecker;
        this.geoService = geoService;
        this.pointUtil = pointUtil;
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
                                        "ST_Distance(ST_Transform({0}, 5181), ST_Transform(ST_GeomFromText({1}, 5181), 5181)) * 100", // 5181 좌표계 사용
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

    @Override
    public void updateWorkplace(WorkplaceRequest updateRequest, Long workplaceId) {
        Workplace existingWorkplace = getExistingWorkplace(workplaceId);

        // 중복 검사 서비스 호출
        workplaceDuplicateChecker.checkForDuplicateName(updateRequest.workplaceName(), workplaceId);
        workplaceDuplicateChecker.checkForDuplicateAddress(updateRequest.workplaceAddress(), workplaceId);
        workplaceDuplicateChecker.checkForDuplicateNumber(updateRequest.workplacePhoneNumber(), workplaceId);

        // 동적 업데이트
        updateWorkplaceInfo(updateRequest, workplaceId, existingWorkplace);
    }

    //사업장 조회
    private Workplace getExistingWorkplace(Long workplaceId) {
        QWorkplace workplace = QWorkplace.workplace;
        Workplace existingWorkplace = queryFactory
                .selectFrom(workplace)
                .where(workplace.workplaceId.eq(workplaceId))
                .fetchOne();

        if (existingWorkplace == null) {
            throw ErrorCode.WORKPLACE_NOT_FOUND.commonException();
        }

        return existingWorkplace;
    }

    // 동적 업데이트
    private void updateWorkplaceInfo(WorkplaceRequest updateRequest, Long workplaceId, Workplace existingWorkplace) {
        QWorkplace workplace = QWorkplace.workplace;
        JPAUpdateClause updateClause = queryFactory.update(workplace);

        boolean isUpdated = false;

        isUpdated |= updateField(updateClause, workplace.workplaceName,
                existingWorkplace.getWorkplaceName(), updateRequest.workplaceName(), WorkplaceName::new);

        isUpdated |= updateField(updateClause, workplace.workplaceAddress,
                existingWorkplace.getWorkplaceAddress(), updateRequest.workplaceAddress(), WorkplaceAddress::new);

        isUpdated |= updateField(updateClause, workplace.workplacePhoneNumber,
                existingWorkplace.getWorkplacePhoneNumber(), updateRequest.workplacePhoneNumber(), WorkplacePhoneNumber::new);


        if (!Objects.equals(existingWorkplace.getWorkplaceAddress(), updateRequest.workplaceAddress())) {
            Map<String, Double> coordinates = geoService.parseAddress(updateRequest.workplaceAddress());
            Point newLocation = pointUtil.createPoint(coordinates.get("longitude"), coordinates.get("latitude"));
            updateClause.set(workplace.location, newLocation);
            isUpdated = true;
        }

        isUpdated |= updateSimpleField(updateClause, workplace.workplaceDescription,
                existingWorkplace.getWorkplaceDescription(), updateRequest.workplaceDescription());

        isUpdated |= updateSimpleField(updateClause, workplace.workplaceStartTime,
                existingWorkplace.getWorkplaceStartTime(), updateRequest.workplaceStartTime());

        isUpdated |= updateSimpleField(updateClause, workplace.workplaceEndTime,
                existingWorkplace.getWorkplaceEndTime(), updateRequest.workplaceEndTime());

        if (isUpdated) {
            updateClause.where(workplace.workplaceId.eq(existingWorkplace.getWorkplaceId())).execute();
        }
    }

    private <T, R> boolean updateField(JPAUpdateClause clause, Path<T> field, T existingValue, R newValue, Function<R, T> converter) {
        if (!Objects.equals(existingValue, converter.apply(newValue))) {
            clause.set(field, converter.apply(newValue));
            return true;
        }
        return false;
    }

    private <T> boolean updateSimpleField(JPAUpdateClause clause, Path<T> field, T existingValue, T newValue) {
        if (!Objects.equals(existingValue, newValue)) {
            clause.set(field, newValue);
            return true;
        }
        return false;
    }
}
