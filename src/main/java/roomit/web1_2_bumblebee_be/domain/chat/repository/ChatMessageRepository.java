package roomit.web1_2_bumblebee_be.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomit.web1_2_bumblebee_be.domain.chat.entity.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoom_RoomId(Long roomId);
    void deleteByTimestampBefore(LocalDateTime cutoffDate);
}