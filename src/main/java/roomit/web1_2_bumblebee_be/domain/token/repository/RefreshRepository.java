package roomit.web1_2_bumblebee_be.domain.token.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import roomit.web1_2_bumblebee_be.domain.token.entity.RefreshEntity;

public interface RefreshRepository extends JpaRepository<RefreshEntity,Long> {

    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);
}
