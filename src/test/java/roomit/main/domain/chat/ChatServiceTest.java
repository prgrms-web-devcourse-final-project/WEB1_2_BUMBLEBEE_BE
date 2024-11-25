package roomit.main.domain.chat;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import roomit.main.domain.chat.dto.ChatMessageRequest;
import roomit.main.domain.chat.dto.ChatMessageResponse;
import roomit.main.domain.chat.entity.ChatMessage;
import roomit.main.domain.chat.repository.ChatMessageRepository;
import roomit.main.domain.chat.service.ChatService;
import roomit.main.domain.chatroom.entity.ChatRoom;
import roomit.main.domain.chatroom.repositoroy.ChatRoomRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChatServiceTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatService chatService;

    private ChatRoom savedRoom;

    @BeforeEach
    void setUp() {
        chatRoomRepository.deleteAll();
        chatMessageRepository.deleteAll();

        // 채팅방 생성
        ChatRoom room = new ChatRoom("Test Room", "user1", LocalDateTime.now());
        savedRoom = chatRoomRepository.save(room);
    }

    @Test
    @DisplayName("채팅 메시지 저장 및 조회")
    @Order(1)
    void saveAndReadMessages() {
        // Given
        ChatMessageRequest request = new ChatMessageRequest(
                savedRoom.getRoomId(),
                "user1",
                "안녕하세요!"
        );

        // When
        ChatMessageResponse savedMessage = chatService.saveMessage(request);
        List<ChatMessageResponse> messages = chatService.getMessagesByRoomId(savedRoom.getRoomId());

        // Then
        assertNotNull(savedMessage);
        assertEquals("안녕하세요!", savedMessage.content());
        assertEquals(1, messages.size());
        assertEquals("안녕하세요!", messages.get(0).content());
    }

    @Test
    @DisplayName("채팅방 생성 및 조회")
    @Order(2)
    void createAndReadChatRoom() {
        // Given
        ChatRoom room = new ChatRoom("New Room", "user2", LocalDateTime.now());

        // When
        ChatRoom savedRoom = chatRoomRepository.save(room);
        ChatRoom foundRoom = chatRoomRepository.findById(savedRoom.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Then
        assertNotNull(foundRoom);
        assertEquals("New Room", foundRoom.getName());
        assertEquals("user2", foundRoom.getCreatedBy());
    }

    @Test
    @DisplayName("30일 이상된 메시지 삭제")
    @Order(3)
    @Transactional
    void deleteOldMessages() {
        // Given
        ChatRoom room = new ChatRoom("Test Room", "user1", LocalDateTime.now());
        ChatRoom savedRoom = chatRoomRepository.save(room);

        ChatMessage oldMessage = new ChatMessage(
                savedRoom, // 채팅방 설정
                "user1",
                "오래된 메시지",
                LocalDateTime.now().minusDays(31)
        );
        ChatMessage recentMessage = new ChatMessage(
                savedRoom, // 채팅방 설정
                "user2",
                "최근 메시지",
                LocalDateTime.now().minusDays(1)
        );

        chatMessageRepository.save(oldMessage);
        chatMessageRepository.save(recentMessage);

        // When
        chatService.deleteOldMessages();
        List<ChatMessage> messages = chatMessageRepository.findByRoom_RoomId(savedRoom.getRoomId());

        // Then
        assertEquals(1, messages.size());
        assertEquals("최근 메시지", messages.get(0).getContent());
    }

    @Test
    @DisplayName("유효하지 않은 채팅방 조회 실패")
    @Order(4)
    void findInvalidChatRoom() {
        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            chatRoomRepository.findById(9999L)
                    .orElseThrow(() -> new RuntimeException("Room not found"));
        });

        assertEquals("Room not found", exception.getMessage());
    }

    @Test
    @DisplayName("채팅 메시지 저장 실패 - 잘못된 방 ID")
    @Order(5)
    void saveMessageWithInvalidRoomId() {
        // Given
        ChatMessageRequest request = new ChatMessageRequest(9999L, "user1", "메시지 내용");

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            chatService.saveMessage(request);
        });

        assertEquals("Room not found", exception.getMessage());
    }

    @Test
    @DisplayName("다수 채팅 메시지 조회")
    @Order(6)
    void readMultipleMessages() {
        // Given
        for (int i = 1; i <= 10; i++) {
            ChatMessage message = new ChatMessage(
                    savedRoom,
                    "user" + i,
                    "메시지 " + i,
                    LocalDateTime.now()
            );
            chatMessageRepository.save(message);
        }

        // When
        List<ChatMessageResponse> messages = chatService.getMessagesByRoomId(savedRoom.getRoomId());

        // Then
        assertNotNull(messages);
        assertEquals(10, messages.size());
        assertEquals("메시지 1", messages.get(0).content());
        assertEquals("메시지 10", messages.get(9).content());
    }
}