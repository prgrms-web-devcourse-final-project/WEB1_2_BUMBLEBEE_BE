package roomit.web1_2_bumblebee_be.domain.studyroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom,Long> {

}
