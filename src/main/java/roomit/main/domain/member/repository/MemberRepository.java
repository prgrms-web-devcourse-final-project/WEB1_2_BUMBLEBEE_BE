package roomit.main.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomit.main.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "SELECT m FROM Member m WHERE m.memberEmail.value=:email")
    Optional<Member> findByMemberEmail(String email);

    @Query(value = "SELECT m FROM Member m WHERE m.memberNickname.value = :nickName")
    Member findByMemberNickName(String nickName);

    @Query("""
        SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END
        FROM Member m
        WHERE m.memberId = :senderId AND m.memberNickname.value = :senderName
    """)
    boolean existsByIdAndMemberNickName(Long senderId, String senderName);
}
