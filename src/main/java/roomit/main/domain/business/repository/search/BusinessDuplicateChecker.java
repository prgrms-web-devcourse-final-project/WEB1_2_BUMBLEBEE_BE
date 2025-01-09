package roomit.main.domain.business.repository.search;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import roomit.main.domain.business.entity.QBusiness;
import roomit.main.global.error.ErrorCode;

@Service
public class BusinessDuplicateChecker {

  private final JPAQueryFactory queryFactory;

  public BusinessDuplicateChecker(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  // 중복 이메일 검사
  public void checkForDuplicateEmail(String businessEmail, Long businessId) {
    QBusiness business = QBusiness.business;
    boolean isEmailDuplicate = queryFactory
        .selectOne()
        .from(business)
        .where(business.businessEmail.value.eq(businessEmail)
            .and(business.businessId.ne(businessId))) // 다른 ID에서 동일 이메일 사용 여부
        .fetchFirst() != null;

    if (isEmailDuplicate) {
      throw ErrorCode.BUSINESS_EMAIL_DUPLICATION.commonException();
    }
  }

  // 중복 사업자 이름 검사
  public void checkForDuplicateBusinessName(String businessName, Long businessId) {
    QBusiness business = QBusiness.business;
    boolean isNameDuplicate = queryFactory
        .selectOne()
        .from(business)
        .where(business.businessName.value.eq(businessName)
            .and(business.businessId.ne(businessId))) // 다른 ID에서 동일 이름 사용 여부
        .fetchFirst() != null;

    if (isNameDuplicate) {
      throw ErrorCode.BUSINESS_NICKNAME_DUPLICATION.commonException();
    }
  }

  // 중복 사업자 번호 검사
  public void checkForDuplicateBusinessNum(String businessNum, Long businessId) {
    QBusiness business = QBusiness.business;
    boolean isNumDuplicate = queryFactory
        .selectOne()
        .from(business)
        .where(business.businessNum.value.eq(businessNum)
            .and(business.businessId.ne(businessId))) // 다른 ID에서 동일 번호 사용 여부
        .fetchFirst() != null;

    if (isNumDuplicate) {
      throw ErrorCode.BUSINESS_NUMBER_DUPLICATION.commonException();
    }
  }
}

