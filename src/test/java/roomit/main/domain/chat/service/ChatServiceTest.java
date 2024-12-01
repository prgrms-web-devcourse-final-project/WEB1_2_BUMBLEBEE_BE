//package roomit.main.domain.chat.service;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.redis.core.RedisTemplate;
//import roomit.main.domain.chat.chatmessage.dto.ChatMessageRequest;
//import roomit.main.domain.chat.chatmessage.dto.ChatMessageResponse;
//import roomit.main.domain.chat.chatmessage.entity.ChatMessage;
//import roomit.main.domain.chat.chatmessage.repository.ChatMessageRepository;
//import roomit.main.domain.chat.chatmessage.service.ChatService;
//import roomit.main.domain.chat.chatroom.entity.ChatRoom;
//import roomit.main.domain.chat.chatroom.repositoroy.ChatRoomRepository;
//import roomit.main.domain.chat.redis.service.RedisLuaService;
//import roomit.main.domain.chat.redis.service.RedisPublisher;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static java.time.LocalDateTime.*;
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//
//@ExtendWith(MockitoExtension.class)
//class ChatServiceTest {
//
//    @InjectMocks
//    private ChatService chatService;
//
//    @Mock
//    private RedisPublisher redisPublisher;
//
//    @Mock
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @Mock
//    private ChatRoomRepository roomRepository;
//
//    @Mock
//    private ChatMessageRepository messageRepository;
//
//    @Mock
//    private RedisLuaService redisLuaService;
//
//    @Test
//    void testSendMessage() {
//        // Given
//        ChatMessageRequest request = new ChatMessageRequest(1L, "Tester", "Hello, Redis!", now());
//
//        // When
//        chatService.sendMessage(request);
//
//        // Then
//        Mockito.verify(redisPublisher, Mockito.times(1)).publish(Mockito.anyString(), Mockito.eq(request));
//        Mockito.verify(redisTemplate.opsForValue(), Mockito.times(1)).set(Mockito.anyString(), Mockito.eq(request));
//    }
//
//    @Test
//    void testFlushMessagesToDatabase() {
//        // Given
//        Long roomId = 1L;
//        ChatMessageRequest messageRequest = new ChatMessageRequest(roomId, "Tester", "Hello, Redis!", now());
//        List<Object> redisData = List.of(List.of("key1", messageRequest));
//
//        Mockito.when(redisLuaService.getKeysAndValues(Mockito.anyString())).thenReturn(redisData);
//        Mockito.when(roomRepository.findById(roomId)).thenReturn(Optional.of(new ChatRoom()));
//
//        // When
//        chatService.flushMessagesToDatabase(roomId);
//
//        // Then
//        Mockito.verify(redisLuaService, Mockito.times(1)).deleteKeys(Mockito.anyString());
//        Mockito.verify(messageRepository, Mockito.times(1)).save(Mockito.any(ChatMessage.class));
//    }
//
//    @Test
//    void testGetMessagesByRoomIdFromRedis() {
//        // Given
//        Long roomId = 1L;
//        ChatMessageRequest messageRequest = new ChatMessageRequest(roomId, "Tester", "Hello, Redis!", now());
//        List<Object> redisData = List.of(List.of("key1", messageRequest));
//
//        Mockito.when(redisLuaService.getKeysAndValues(Mockito.anyString())).thenReturn(redisData);
//
//        // When
//        List<ChatMessageResponse> responses = chatService.getMessagesByRoomId(roomId);
//
//        // Then
//        assertThat(responses).isNotEmpty();
//        assertThat(responses.get(0).content()).isEqualTo("Hello, Redis!");
//    }
//}