package roomit.main.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMessageBatchProcessor {
    private final ChatService chatService;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void flushMessages() {
        // 필요한 Room ID를 기준으로 데이터를 처리 (예: 모든 Room ID 처리)
        List<Long> roomIds = List.of(1L, 2L, 3L); // 예: 모든 채팅방 ID를 동적으로 가져오는 로직 필요
        for (Long roomId : roomIds) {
            chatService.flushMessagesToDatabase(roomId);
        }
    }
}
