package roomit.web1_2_bumblebee_be.domain.studyroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import roomit.web1_2_bumblebee_be.domain.studyroom.dto.response.FindPossibleStudyRoomResponse;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom,Long> {
    @Query("SELECT sr FROM StudyRoom sr JOIN sr.workPlaceId wp WHERE sr.capacity >= :capacity AND wp.workplaceAddress = :workplaceAddress AND NOT EXISTS (SELECT r.studyRoomId FROM Reservation r WHERE r.studyRoomId = sr AND (r.startTime < :endTime AND r.endTime > :startTime))")
    List<FindPossibleStudyRoomResponse> findAvailableStudyRooms(@Param("workplaceAddress") String workplaceAddress, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("capacity") Integer capacity);

    @Query("SELECT sr FROM StudyRoom sr JOIN sr.workPlaceId wp WHERE wp.workplaceId = :workplaceId")
    List<StudyRoom> findStudyRoomsByWorkPlaceId(@Param("workplaceId") Long workplaceId);

}