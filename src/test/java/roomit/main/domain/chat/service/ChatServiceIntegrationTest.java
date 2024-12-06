//package roomit.main.domain.chat.service;
//
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import roomit.main.domain.business.dto.CustomBusinessDetails;
//import roomit.main.domain.business.entity.Business;
//import roomit.main.domain.business.repository.BusinessRepository;
//import roomit.main.domain.chat.chatmessage.repository.ChatMessageRepository;
//import roomit.main.domain.chat.chatmessage.service.ChatService;
//import roomit.main.domain.chat.chatroom.dto.response.ChatRoomBusinessResponse;
//import roomit.main.domain.chat.chatroom.dto.response.ChatRoomMemberResponse;
//import roomit.main.domain.chat.chatroom.dto.response.ChatRoomResponse;
//import roomit.main.domain.chat.chatroom.repositoroy.ChatRoomRepository;
//import roomit.main.domain.chat.chatroom.service.ChatRoomService;
//import roomit.main.domain.member.dto.CustomMemberDetails;
//import roomit.main.domain.member.entity.Member;
//import roomit.main.domain.member.entity.Sex;
//import roomit.main.domain.member.repository.MemberRepository;
//import roomit.main.domain.studyroom.entity.StudyRoom;
//import roomit.main.domain.studyroom.repository.StudyRoomRepository;
//import roomit.main.domain.workplace.entity.Workplace;
//import roomit.main.domain.workplace.repository.WorkplaceRepository;
//import roomit.main.global.service.ImageService;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@Transactional
//@SpringBootTest
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class ChatServiceIntegrationTest {
//
//    @Autowired
//    private ChatService chatService;
//
//    @Autowired
//    private ChatRoomService chatRoomService;
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
//    @Autowired
//    private StudyRoomRepository studyRoomRepository;
//
//    @Autowired
//    private WorkplaceRepository workplaceRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    @Autowired
//    private ImageService imageService;
//
//    private Business business;
//
//    private Member member;
//
//    private Workplace workplace;
//
//    private StudyRoom studyRoom;
//
//    @BeforeAll
//    void setUp() {
//        businessRepository.deleteAll();
//        memberRepository.deleteAll();
//        chatMessageRepository.deleteAll();
//        chatRoomRepository.deleteAll();
//
//        String email = "business12@gmail.com";
//
//        // 고유한 business_email로 설정
//        business = Business.builder()
//                .businessName("테스트사업자")
//                .businessEmail(email)
//                .businessPwd("Business1!")
//                .businessNum("123-12-12345")
//                .passwordEncoder(bCryptPasswordEncoder)
//                .build();
//
//        businessRepository.save(business);
//
//        member = Member.builder()
//                .birthDay(LocalDate.of(2024, 11, 22))
//                .memberSex(Sex.FEMALE)
//                .memberPwd("Business1!")
//                .memberEmail("member1@naver.com")
//                .memberPhoneNumber("010-1323-2154")
//                .memberNickName("테스트유저")
//                .passwordEncoder(bCryptPasswordEncoder)
//                .build();
//
//        memberRepository.save(member);
//
//        workplace = Workplace.builder()
//                .workplaceName("Workplace")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .imageUrl(imageService.createImageUrl("Workplace"))
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .business(business)
//                .build();
//        workplaceRepository.save(workplace);
//
//        studyRoom = StudyRoom.builder()
//                .studyRoomName("Test Room")
//                .description("A test room")
//                .capacity(10)
//                .price(100)
//                .imageUrl(imageService.createImageUrl("Workplace/Test Room"))
//                .workplace(workplace)
//                .build();
//
//        studyRoomRepository.save(studyRoom);
//    }
//
//
//    @Test
//    @DisplayName("redis 연결 테스트")
//    void testRedisConnection() {
//        redisTemplate.opsForValue().set("test-key", "test-value");
//        String value = redisTemplate.opsForValue().get("test-key");
//        System.out.println("Retrieved value from Redis: " + value);
//        assertEquals("test-value", value);
//    }
//
//    @Test
//    @DisplayName("채팅방 생성 테스트 - 성공")
//    void createChatRoom() {
//        chatRoomRepository.deleteAll();
//
//        //Given
//        Long memberId = member.getMemberId();
//        Long studyRoomId = studyRoom.getStudyRoomId();
//
//        // When
//        chatRoomService.create(memberId, studyRoomId);
//        //then
//        boolean chatRoomExists = chatRoomRepository.existsChatRoomByMemberIdAndBusinessId(memberId, business.getBusinessId());
//        Assertions.assertTrue(chatRoomExists, "ChatRoom이 생성되지 않았습니다.");
//
//        List<ChatRoomResponse> createMemberChatRoomList = chatRoomRepository.findChatRoomByMembersId(memberId);
//
////        Assertions.assertEquals(1,createMemberChatRoomList.size());
//        for (ChatRoomResponse chatRoom : createMemberChatRoomList) {
//            ChatRoomMemberResponse chatRoomMemberResponse = (ChatRoomMemberResponse) chatRoom;
//            Assertions.assertEquals(chatRoomMemberResponse.businessId(), business.getBusinessId());
//        }
//
//        List<ChatRoomResponse> createBusinessChatRoomList = chatRoomRepository.findChatRoomByBusinessId(business.getBusinessId());
//
////        Assertions.assertEquals(1,createBusinessChatRoomList.size());
//        for (ChatRoomResponse chatRoom : createBusinessChatRoomList) {
//            ChatRoomBusinessResponse chatRoomMemberResponse = (ChatRoomBusinessResponse) chatRoom;
//            Assertions.assertEquals(chatRoomMemberResponse.memberId(), memberId);
//        }
//    }
//
//    @Test
//    @DisplayName("이미 존재하는 채팅방")
//    void createChatRoom_already() {
//        //Given
//        Long memberId = member.getMemberId();
//        Long studyRoomId = studyRoom.getStudyRoomId();
//        chatRoomService.create(memberId, studyRoomId);
//
////        //When & Then
////        Assertions.assertThrows(CommonException.class, () -> {
////            chatRoomService.create(memberId, studyRoomId);
////        });
//    }
//
//    @Test
//    @DisplayName("채팅방 조회 - member")
//    void getRoomsA() {
//        //Given
//        Long memberId = member.getMemberId();
//        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);
//
//        List<ChatRoomMemberResponse> chatRoomMemberResponses = List.of(
//                new ChatRoomMemberResponse(1L, 1L, LocalDateTime.now()),
//                new ChatRoomMemberResponse(2L, 2L, LocalDateTime.now())
//        );
//
//        List<ChatRoomResponse> chatRoom = chatRoomRepository.findChatRoomByMembersId(memberId);
//
//        //When
//        List<ChatRoomResponse> result = chatRoomService.getRooms(customMemberDetails, null);
//
//        //Then
//        assertNotNull(result);
//        assertEquals(chatRoom.size(), result.size());
//    }
//
//    @Test
//    @DisplayName("채팅방 조회 - business")
//    void getRoomsB() {
//        //Given
//        Long businessId = business.getBusinessId();
//        CustomBusinessDetails customBusinessDetails = new CustomBusinessDetails(business);
//
//        List<ChatRoomBusinessResponse> chatRoomMemberResponses = List.of(
//                new ChatRoomBusinessResponse(1L, 1L, member.getMemberNickName(), LocalDateTime.now()),
//                new ChatRoomBusinessResponse(2L, 2L, member.getMemberNickName(), LocalDateTime.now())
//        );
//
//        List<ChatRoomResponse> chatRoom = chatRoomRepository.findChatRoomByBusinessId(businessId);
//
//        //When
//        List<ChatRoomResponse> result = chatRoomService.getRooms(null, customBusinessDetails);
//
//        //Then
//        assertNotNull(result);
//        assertEquals(chatRoom.size(), result.size());
//    }
//
////    @Test
////    void testSendMessage() {
////
////        // Given
////        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(business, member));
////        ChatMessageRequest request = new ChatMessageRequest(chatRoom.getRoomId(), "Tester", "Hello, Redis!", LocalDateTime.now(), "member");
////        System.out.println("ChatRoom saved with ID: " + chatRoom.getRoomId());
////
////        // When
////        chatService.sendMessage(request);
////        System.out.println("Message sent to Redis: " + request);
////
////        chatService.flushMessagesToDatabase(chatRoom.getRoomId());
////        System.out.println("Flushed messages from Redis to MySQL for Room ID: " + chatRoom.getRoomId());
////
////        // Then
////        List<ChatMessage> messages = chatMessageRepository.findAll();
////        System.out.println("Messages in MySQL: " + messages);
////        assertThat(messages).isNotEmpty(); // 데이터가 존재하는지 확인
////
////        ChatMessage savedMessage = messages.get(0);
////        assertThat(savedMessage.getSender()).isEqualTo("Tester");
////        assertThat(savedMessage.getContent()).isEqualTo("Hello, Redis!");
////    }
//}
