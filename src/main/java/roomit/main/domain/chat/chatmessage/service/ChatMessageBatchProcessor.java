package roomit.main.domain.chat.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import roomit.main.domain.chat.chatroom.repository.ChatRoomRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMessageBatchProcessor {
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;

    @Scheduled(fixedRate = 10000) // 1분마다 실행
    public void flushMessages() {
        List<Long> roomIds = chatRoomRepository.findAllRoomIds(); // Room ID 동적 조회
        for (Long roomId : roomIds) {
            chatService.flushMessagesToDatabase(roomId);
        }
    }
}
