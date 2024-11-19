package roomit.web1_2_bumblebee_be.domain.studyroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyroomEntity;

public interface StudyroomRepository extends JpaRepository<StudyroomEntity,Long> {

}
