package roomit.main.domain.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.business.repository.BusinessRepository;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageRequest;
import roomit.main.domain.chat.chatmessage.repository.ChatMessageRepository;
import roomit.main.domain.chat.chatmessage.service.ChatService;
import roomit.main.domain.chat.chatroom.repositoroy.ChatRoomRepository;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.entity.Sex;
import roomit.main.domain.member.repository.MemberRepository;

@SpringBootTest
@ActiveProfiles("test")
class ChatServiceIntegrationTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Business business;

    private Member member;

    @BeforeEach
    void setUp() {
        businessRepository.deleteAll();
        memberRepository.deleteAll();

        String email = "business12@gmail.com";

        // 고유한 business_email로 설정
        business = Business.builder()
                .businessName("테스트사업자")
                .businessEmail(email)
                .businessPwd("Business1!")
                .businessNum("123-12-12345")
                .passwordEncoder(bCryptPasswordEncoder)
                .build();

        businessRepository.save(business);

        member =  Member.builder()
                .birthDay(LocalDate.of(2024, 11, 22))
                .memberSex(Sex.FEMALE)
                .memberPwd("Business1!")
                .memberEmail("member1@naver.com")
                .memberPhoneNumber("010-1323-2154")
                .memberNickName("테스트유저")
                .passwordEncoder(bCryptPasswordEncoder)
                .build();

        memberRepository.save(member);
    }


    @Test
    void testRedisConnection() {
        redisTemplate.opsForValue().set("test-key", "test-value");
        String value = redisTemplate.opsForValue().get("test-key");
        System.out.println("Retrieved value from Redis: " + value);
        assertEquals("test-value", value);
    }

    @Test
    void testSerialization() throws IOException {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        ChatMessageRequest originalMessage = new ChatMessageRequest(1L, "Tester", "Hello, Redis!", LocalDateTime.now(), "member");

        // Serialize
        byte[] serialized = serializer.serialize(originalMessage);
        System.out.println("Serialized data: " + new String(serialized));

        // Deserialize explicitly using ObjectMapper
        ChatMessageRequest deserialized = objectMapper.readValue(serialized, ChatMessageRequest.class);
        System.out.println("Deserialized object: " + deserialized);

        // Then
        assertThat(deserialized).isEqualTo(originalMessage);
    }




//    @Test
//    void testSendMessageAndFlush() {
//        // Given
//        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(business, member));
//        ChatMessageRequest request = new ChatMessageRequest(chatRoom.getRoomId(), "Tester", "Hello, Redis!", LocalDateTime.now(), "member");
//        System.out.println("ChatRoom saved with ID: " + chatRoom.getRoomId());
//
//        // When
//        chatService.sendMessage(request);
//        System.out.println("Message sent to Redis: " + request);
//
//        chatService.flushMessagesToDatabase(chatRoom.getRoomId());
//        System.out.println("Flushed messages from Redis to MySQL for Room ID: " + chatRoom.getRoomId());
//
//        // Then
//        List<ChatMessage> messages = chatMessageRepository.findAll();
//        System.out.println("Messages in MySQL: " + messages);
//        assertThat(messages).isNotEmpty(); // 데이터가 존재하는지 확인
//
//        ChatMessage savedMessage = messages.get(0);
//        assertThat(savedMessage.getSender()).isEqualTo("Tester");
//        assertThat(savedMessage.getContent()).isEqualTo("Hello, Redis!");
//    }
}
