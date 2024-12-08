package roomit.main.domain.chat.chatmessage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomit.main.domain.chat.chatmessage.dto.request.ChatMessageSaveRequest;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]")
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(value = EnumType.STRING)
    private SenderType senderType;

    private Boolean isRead = false;

    public void markAsRead() {
        this.isRead = true;
    }

    public ChatMessage(ChatRoom room, ChatMessageSaveRequest request) {
        this.room = room;
        this.sender = request.sender();
        this.content = request.content();
        this.senderType = request.senderType();
        this.timestamp = request.timestamp();
    }
}
