package roomit.main.global.token.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import roomit.main.global.token.entity.Refresh;

@Repository
public interface RefreshRepository extends CrudRepository<Refresh,String> {
}
