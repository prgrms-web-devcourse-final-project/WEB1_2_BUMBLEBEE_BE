package roomit.web1_2_bumblebee_be.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "SELECT m FROM Member m WHERE m.memberEmail.value=:email")
    Optional<Member> findByMemberEmail(String email);
}