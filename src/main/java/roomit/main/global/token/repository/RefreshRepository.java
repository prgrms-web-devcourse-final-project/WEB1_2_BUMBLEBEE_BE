package roomit.main.global.token.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import roomit.main.global.token.entity.RefreshEntity;

public interface RefreshRepository extends JpaRepository<RefreshEntity,Long> {

    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);
}
