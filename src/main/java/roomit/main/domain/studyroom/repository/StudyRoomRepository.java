package roomit.main.domain.studyroom.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import roomit.main.domain.studyroom.dto.response.FindPossibleStudyRoomResponse;
import roomit.main.domain.studyroom.entity.StudyRoom;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom,Long> {
    @Query("SELECT sr FROM StudyRoom sr JOIN sr.workPlace wp WHERE sr.capacity >= :capacity AND wp.workplaceAddress = :workplaceAddress AND NOT EXISTS (SELECT r FROM Reservation r WHERE r.studyRoom = sr AND (r.startTime < :endTime AND r.endTime > :startTime))")
    List<FindPossibleStudyRoomResponse> findAvailableStudyRooms(@Param("workplaceAddress") String workplaceAddress, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("capacity") Integer capacity);

    @Query("SELECT sr FROM StudyRoom sr JOIN sr.workPlace wp WHERE wp.workplaceId = :workplaceId")
    List<StudyRoom> findStudyRoomsByWorkPlaceId(@Param("workplaceId") Long workplaceId);

}