package roomit.main.domain.workplace.repository.search;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import roomit.main.domain.workplace.entity.QWorkplace;
import roomit.main.global.error.ErrorCode;

@Service
public class WorkplaceDuplicateChecker {

    private final JPAQueryFactory queryFactory;

    public WorkplaceDuplicateChecker(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public void checkForDuplicateName(String workplaceName, Long workplaceId) {
        QWorkplace workplace = QWorkplace.workplace;
        boolean isNameDuplicate = queryFactory
                            .selectOne()
                            .from(workplace)
                            .where(workplace.workplaceName.value.eq(workplaceName)
                                    .and(workplace.workplaceId.ne(workplaceId)))
                            .fetchFirst() != null;

        if (isNameDuplicate) {
            throw ErrorCode.WORKPLACE_NAME_DUPLICATION.commonException();
        }
    }

    public void checkForDuplicateAddress(String workplaceAddress, Long workplaceId) {
        QWorkplace workplace = QWorkplace.workplace;
        boolean isAddressDuplicate = queryFactory
                                          .selectOne()
                                          .from(workplace)
                                          .where(workplace.workplaceAddress.value.eq(workplaceAddress)
                                                  .and(workplace.workplaceId.ne(workplaceId)))
                                          .fetchFirst() != null;

        if (isAddressDuplicate) {
            throw ErrorCode.WORKPLACE_ADDRESS_DUPLICATION.commonException();
        }
    }

    public void checkForDuplicateNumber(String workplacePhoneNumber, Long workplaceId) {
        QWorkplace workplace = QWorkplace.workplace;
        boolean isPhoneNumberDuplicate = queryFactory
                                          .selectOne()
                                          .from(workplace)
                                          .where(workplace.workplacePhoneNumber.value.eq(workplacePhoneNumber)
                                                  .and(workplace.workplaceId.ne(workplaceId)))
                                          .fetchFirst() != null;

        if (isPhoneNumberDuplicate) {
            throw ErrorCode.WORKPLACE_PHONE_NUMBER_DUPLICATION.commonException();
        }
    }


}
