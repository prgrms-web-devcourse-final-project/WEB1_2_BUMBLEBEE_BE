package roomit.main.domain.business.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.business.repository.search.SearchBusiness;

public interface BusinessRepository extends JpaRepository<Business, Long> , SearchBusiness {

    @Query(value = "SELECT b FROM Business b WHERE b.businessEmail.value=:businessEmail")
    Optional<Business> findByBusinessEmail(String businessEmail);

    @Query("""
	    SELECT CASE WHEN EXISTS (
	        SELECT 1
	        FROM Business b
	        WHERE b.businessName.value = :businessName
	    ) THEN TRUE ELSE FALSE END
	""")
    Boolean existsByBusinessName(String businessName);

    @Query("""
	    SELECT CASE WHEN EXISTS (
	        SELECT 1
	        FROM Business b
	        WHERE b.businessEmail.value = :businessEmail
	    ) THEN TRUE ELSE FALSE END
	""")
    Boolean existsByBusinessEmail(String businessEmail);

    @Query("""
	    SELECT CASE WHEN EXISTS (
	        SELECT 1
	        FROM Business b
	        WHERE b.businessNum.value = :businessNum
	    ) THEN TRUE ELSE FALSE END
	""")
    Boolean existsByBusinessNum(String businessNum);
}

