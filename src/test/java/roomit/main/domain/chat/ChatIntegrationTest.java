package roomit.main.domain.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageRequest;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;
import roomit.main.domain.chat.chatroom.repositoroy.ChatRoomRepository;
import roomit.main.domain.chat.chatmessage.service.ChatService;

import java.time.LocalDateTime;

@SpringBootTest
public class ChatIntegrationTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private Long testRoomId;

    @BeforeEach
    public void setup() {
        // ChatRoom 생성
        ChatRoom chatRoom = new ChatRoom("Test Room", "Tester", LocalDateTime.now());
        chatRoomRepository.save(chatRoom);
        testRoomId = chatRoom.getRoomId();
    }

    @Test
    public void testFlushMessagesToDatabase() {
        // Redis에 메시지 저장
        ChatMessageRequest request = new ChatMessageRequest(testRoomId, "Tester", "Hello, Redis!", LocalDateTime.now());
        chatService.sendMessage(request);

        // Redis에서 메시지 MySQL로 저장
        chatService.flushMessagesToDatabase(testRoomId);

        // Assertions 추가
        // MySQL에서 데이터가 정상적으로 저장되었는지 검증
    }
}
