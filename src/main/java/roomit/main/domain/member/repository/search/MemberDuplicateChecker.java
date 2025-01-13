package roomit.main.domain.member.repository.search;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import roomit.main.domain.business.entity.QBusiness;
import roomit.main.domain.member.entity.QMember;
import roomit.main.global.error.ErrorCode;

@Service
public class MemberDuplicateChecker {

  private final JPAQueryFactory queryFactory;

  public MemberDuplicateChecker(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  public void checkForDuplicateEmail(String email, Long memberId){
    QMember member = QMember.member;
    boolean isEmailDuplicate = queryFactory
            .selectOne()
            .from(member)
            .where(member.memberEmail.value.eq(email)
                    .and(member.memberId.ne(memberId)))
            .fetchFirst() != null;

    if(isEmailDuplicate){
      throw ErrorCode.MEMBER_DUPLICATE_EMAIL.commonException();
    }
  }

  public void checkForDuplicateNickName(String nickName, Long memberId){
    QMember member = QMember.member;
    boolean isEmailDuplicate = queryFactory
            .selectOne()
            .from(member)
            .where(member.memberNickname.value.eq(nickName)
                    .and(member.memberId.ne(memberId)))
            .fetchFirst() != null;

    if(isEmailDuplicate){
      throw ErrorCode.MEMBER_DUPLICATE_NICK_NAME.commonException();
    }
  }

  public void checkForDuplicatePhoneNumber(String phoneNumber, Long memberId){
    QMember member = QMember.member;
    boolean isEmailDuplicate = queryFactory
            .selectOne()
            .from(member)
            .where(member.memberPhonenumber.value.eq(phoneNumber)
                    .and(member.memberId.ne(memberId)))
            .fetchFirst() != null;

    if(isEmailDuplicate){
      throw ErrorCode.MEMBER_DUPLICATE_PHONENUMBER.commonException();
    }
  }



}

