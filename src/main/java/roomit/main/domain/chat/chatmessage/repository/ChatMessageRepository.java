package roomit.main.domain.chat.chatmessage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomit.main.domain.chat.chatmessage.entity.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.room.roomId = :roomId")
    List<ChatMessage> findByRoomId(Long roomId);
    void deleteByTimestampBefore(LocalDateTime cutoffDate);
}