package roomit.main.domain.business.repository.search;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import java.util.Objects;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.business.dto.request.BusinessUpdateRequest;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.business.entity.QBusiness;
import roomit.main.domain.business.entity.value.BusinessEmail;
import roomit.main.domain.business.entity.value.BusinessNickname;
import roomit.main.domain.business.entity.value.BusinessNum;
import roomit.main.global.error.ErrorCode;

@Repository
public class SearchBusinessImpl implements SearchBusiness {
  private final BusinessDuplicateChecker businessDuplicateChecker;
  private final JPAQueryFactory queryFactory;

  public SearchBusinessImpl(JPAQueryFactory queryFactory, BusinessDuplicateChecker businessDuplicateChecker) {
    this.queryFactory = queryFactory;
    this.businessDuplicateChecker = businessDuplicateChecker;
  }

  @Transactional
  public void updateBusiness(BusinessUpdateRequest updateRequest, Long businessId) {

    Business existingBusiness = getExistingBusiness(businessId);

    // 중복 검사 서비스 호출
    businessDuplicateChecker.checkForDuplicateEmail(updateRequest.businessEmail(), businessId);
    businessDuplicateChecker.checkForDuplicateBusinessName(updateRequest.businessName(), businessId);
    businessDuplicateChecker.checkForDuplicateBusinessNum(updateRequest.businessNum(), businessId);

    // 동적 업데이트
    updateBusinessInfo(updateRequest, businessId, existingBusiness);
  }

  //사업자 조회
  private Business getExistingBusiness(Long businessId) {
    QBusiness business = QBusiness.business;
    Business existingBusiness = queryFactory
        .selectFrom(business)
        .where(business.businessId.eq(businessId))
        .fetchOne();

    if (existingBusiness == null) {
      throw ErrorCode.BUSINESS_NOT_FOUND.commonException();
    }

    return existingBusiness;
  }

  // 동적 업데이트
  private void updateBusinessInfo(BusinessUpdateRequest updateRequest, Long businessId, Business existingBusiness) {
    QBusiness business = QBusiness.business;
    JPAUpdateClause updateClause = queryFactory.update(business);

    boolean isUpdated = false; // 변경된 필드 여부 체크

    // 변경된 필드만 업데이트
    if (!Objects.equals(existingBusiness.getBusinessName(), updateRequest.businessName())) {
      updateClause.set(business.businessName, new BusinessNickname(updateRequest.businessName()));
      isUpdated = true; // 변경된 필드가 있으면 true
    }
    if (!Objects.equals(existingBusiness.getBusinessEmail(), updateRequest.businessEmail())) {
      updateClause.set(business.businessEmail, new BusinessEmail(updateRequest.businessEmail()));
      isUpdated = true; // 변경된 필드가 있으면 true
    }
    if (!Objects.equals(existingBusiness.getBusinessNum(), updateRequest.businessNum())) {
      updateClause.set(business.businessNum, new BusinessNum(updateRequest.businessNum()));
      isUpdated = true; // 변경된 필드가 있으면 true
    }

    // 변경된 필드가 있을 경우에만 업데이트 실행
    if (isUpdated) {
      updateClause.where(business.businessId.eq(businessId)).execute();
    }
  }

}
