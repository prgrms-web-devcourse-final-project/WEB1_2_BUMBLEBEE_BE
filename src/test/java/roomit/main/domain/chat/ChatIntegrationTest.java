//package roomit.main.domain.chat;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import roomit.main.domain.business.entity.Business;
//import roomit.main.domain.business.repository.BusinessRepository;
//import roomit.main.domain.chat.chatmessage.dto.ChatMessageRequest;
//import roomit.main.domain.chat.chatmessage.entity.ChatMessage;
//import roomit.main.domain.chat.chatmessage.repository.ChatMessageRepository;
//import roomit.main.domain.chat.chatmessage.service.ChatService;
//import roomit.main.domain.chat.chatroom.entity.ChatRoom;
//import roomit.main.domain.chat.chatroom.repositoroy.ChatRoomRepository;
//import roomit.main.domain.member.entity.Member;
//import roomit.main.domain.member.repository.MemberRepository;
//
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class ChatIntegrationTest {
//
//    @Autowired
//    private ChatService chatService;
//
//    @Autowired
//    private ChatRoomRepository chatRoomRepository;
//
//    @Autowired
//    private ChatMessageRepository chatMessageRepository;
//
//    @Autowired
//    private BusinessRepository businessRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    private Long testRoomId;
//
//    @BeforeEach
//    public void setup() {
//        // 테스트용 Business 생성
//        Business business = new Business("Test Business", "test@business.com", "1234", "TEST123");
//        businessRepository.save(business);
//
//        // 테스트용 Member 생성
//        Member member = new Member("test@member.com", "1234", "Tester", "010-1234-5678");
//        memberRepository.save(member);
//
//        // ChatRoom 생성
//        ChatRoom chatRoom = new ChatRoom(business, member, LocalDateTime.now());
//        chatRoomRepository.save(chatRoom);
//        testRoomId = chatRoom.getRoomId();
//    }
//
//    @Test
//    public void testFlushMessagesToDatabase() {
//        // Given: Redis에 메시지 저장
//        ChatMessageRequest request = new ChatMessageRequest(
//                testRoomId,
//                "Tester",
//                "Hello, Redis!",
//                LocalDateTime.now()
//        );
//        chatService.sendMessage(request);
//
//        // When: Redis에서 메시지를 MySQL로 플러시
//        chatService.flushMessagesToDatabase(testRoomId);
//
//        // Then: MySQL에서 데이터 검증
//        ChatMessage savedMessage = chatMessageRepository.findAll().get(0);
//        assertThat(savedMessage).isNotNull();
//        assertThat(savedMessage.getRoom().getRoomId()).isEqualTo(testRoomId);
//        assertThat(savedMessage.getSender()).isEqualTo("Tester");
//        assertThat(savedMessage.getContent()).isEqualTo("Hello, Redis!");
//    }
//}
