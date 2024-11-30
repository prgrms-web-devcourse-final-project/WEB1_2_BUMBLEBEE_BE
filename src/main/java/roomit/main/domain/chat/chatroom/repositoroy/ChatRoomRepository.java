package roomit.main.domain.chat.chatroom.repositoroy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c.roomId FROM ChatRoom c")
    List<Long> findAllRoomIds();

}