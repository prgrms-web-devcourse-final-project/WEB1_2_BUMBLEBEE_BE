package roomit.main.domain.member.repository.search;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.member.dto.request.MemberUpdateRequest;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.entity.QMember;
import roomit.main.domain.member.entity.value.MemberEmail;
import roomit.main.domain.member.entity.value.MemberNickname;
import roomit.main.domain.member.entity.value.MemberPhoneNumber;
import roomit.main.global.error.ErrorCode;

@RequiredArgsConstructor
@Repository
public class SearchMemberImpl implements SearchMember {
    private final MemberDuplicateChecker memberDuplicateChecker;
    private final JPAQueryFactory queryFactory;


    public void updateMember(MemberUpdateRequest memberUpdateRequest, Long memberId) {
        Member member = getMember(memberId);

        memberDuplicateChecker.checkForDuplicateEmail(memberUpdateRequest.email(), memberId);
        memberDuplicateChecker.checkForDuplicateNickName(memberUpdateRequest.nickName(), memberId);
        memberDuplicateChecker.checkForDuplicatePhoneNumber(memberUpdateRequest.phoneNumber(), memberId);


        updateMemberInfo(memberUpdateRequest, memberId, member);

    }

    private Member getMember(Long memberId){
        QMember member = QMember.member;
        Member foundMember = queryFactory.selectFrom(member)
                .where(member.memberId.eq(memberId))
                .fetchOne();

        if(foundMember == null){
            throw ErrorCode.MEMBER_NOT_FOUND.commonException();
        }
        return foundMember;
    }

    private void updateMemberInfo(MemberUpdateRequest memberUpdateRequest, Long memberId, Member member){
        QMember qMember = QMember.member;
        JPAUpdateClause update = queryFactory.update(qMember);

        if(!memberUpdateRequest.nickName().equals(member.getMemberNickName())){
            update.set(qMember.memberNickname, new MemberNickname(memberUpdateRequest.nickName()));
        }
        if(!memberUpdateRequest.phoneNumber().equals(member.getMemberNickName())){
            update.set(qMember.memberPhonenumber, new MemberPhoneNumber(memberUpdateRequest.phoneNumber()));
        }
        if(!memberUpdateRequest.email().equals(member.getMemberEmail())){
            update.set(qMember.memberEmail, new MemberEmail(memberUpdateRequest.email()));
        }
        if(!memberUpdateRequest.sex().equals(member.getMemberSex())){
            update.set(qMember.memberSex, memberUpdateRequest.sex());
        }
        if(!memberUpdateRequest.birthDay().equals(member.getBirthDay())){
            update.set(qMember.birthDay, memberUpdateRequest.birthDay());
        }
        update.where(qMember.memberId.eq(memberId)).execute();
    }
}
