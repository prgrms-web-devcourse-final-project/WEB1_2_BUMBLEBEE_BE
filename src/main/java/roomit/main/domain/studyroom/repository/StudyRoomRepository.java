package roomit.main.domain.studyroom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.repository.search.SearchStudyRoom;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom,Long>, SearchStudyRoom {

    @Query("SELECT sr FROM StudyRoom sr JOIN sr.workPlace wp WHERE wp.workplaceId = :workplaceId")
    List<StudyRoom> findStudyRoomsByWorkPlaceId(@Param("workplaceId") Long workplaceId);


    @Query("SELECT sr FROM StudyRoom sr JOIN FETCH sr.workPlace WHERE sr.studyRoomId = :studyRoomId")
    Optional<StudyRoom> findByIdWithWorkplace(@Param("studyRoomId") Long studyRoomId);

}