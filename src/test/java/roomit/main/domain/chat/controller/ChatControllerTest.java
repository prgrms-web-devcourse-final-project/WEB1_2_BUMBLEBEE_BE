package roomit.main.domain.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageRequest;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.entity.Sex;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.token.dto.LoginRequest;
import roomit.main.domain.token.dto.LoginResponse;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    private static final String SEND_MESSAGE_ENDPOINT = "/app/chat/sendMessage";
    private static final String SUBSCRIBE_ROOM = "/sub/chat/room/1";

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port; // 동적 포트 처리

    private WebSocketStompClient stompClient;
    private BlockingQueue<String> blockingQueue;

    private static String token;

    @BeforeAll
    void setup() throws Exception {
        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new org.springframework.messaging.converter.MappingJackson2MessageConverter());

        Member member =  Member.builder()
                .birthDay(LocalDate.now())
                .memberSex(Sex.FEMALE)
                .memberPwd("Business1!")
                .memberEmail("qwdfasdf@naver.com")
                .memberPhoneNumber("010-1323-2154")
                .memberNickName("치킨유저")
                .passwordEncoder(bCryptPasswordEncoder)
                .build();

        memberRepository.save(member);

        LoginRequest loginRequest = LoginRequest.builder()
                .email("qwdfasdf@naver.com")
                .password("Business1!")
                .build();

        String json = objectMapper.writeValueAsString(loginRequest);

        MvcResult loginResult = mockMvc.perform(post("/login/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        LoginResponse loginResponse = objectMapper.readValue(loginResult.getResponse().getContentAsString(), LoginResponse.class);
        token = loginResponse.getToken();

        System.out.println(token);
    }

    @Test
    void testSendMessageAndReceive() throws Exception {
        // WebSocket URL 동적 생성
        String websocketUrl = String.format("ws://localhost:%d/ws", port);

        System.out.println("Connecting to WebSocket: " + websocketUrl);

        // WebSocket 세션 연결
        StompSession session = stompClient.connect(websocketUrl, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession stompSession, StompHeaders connectedHeaders) {
                connectedHeaders.add("Authorization", token);
            }
        }).get(1, TimeUnit.SECONDS);

        session.subscribe(SUBSCRIBE_ROOM, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("Received payload: " + payload);
                blockingQueue.offer((String) payload);
            }
        });

        // 메시지 생성
        ChatMessageRequest request = ChatMessageRequest.builder()
                .roomId(1L)
                .sender("Tester")
                .content("Hello, WebSocket!")
                .timestamp(LocalDateTime.now())
                .build();

        // 메시지 전송
        String message = objectMapper.writeValueAsString(request);
        System.out.println("Sending message: " + message);

        session.send(SEND_MESSAGE_ENDPOINT, message);

        // 메시지 검증
        String receivedMessage = blockingQueue.poll(5, TimeUnit.SECONDS);
        System.out.println("Received message from WebSocket: " + receivedMessage);

        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage).contains("Hello, WebSocket!");
    }

    private List<Transport> createTransportClient() {
        return List.of(new WebSocketTransport(new StandardWebSocketClient()));
    }

}
