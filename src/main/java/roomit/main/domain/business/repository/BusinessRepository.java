package roomit.main.domain.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomit.main.domain.business.entity.Business;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {

    @Query(value = "SELECT b FROM Business b WHERE b.businessEmail.value=:businessEmail")
    Optional<Business> findByBusinessEmail(String businessEmail);

    @Query(value = "SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Business b WHERE b.businessName.value=:businessName")
    boolean existsByBusinessName(String businessName);

    @Query(value = "SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Business b WHERE b.businessEmail.value=:businessEmail")
    Boolean existsByBusinessEmail(String businessEmail);

    @Query(value = "SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Business b WHERE b.businessName.value=:businessNum")
    Boolean existsByBusinessNum(String businessNum);


}

