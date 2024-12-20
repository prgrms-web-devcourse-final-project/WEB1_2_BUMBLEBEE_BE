package roomit.main.global.config.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import roomit.main.domain.chat.chatmessage.service.ChatService;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final ChatService chatService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    public void deleteOldMessages() {
        chatService.deleteOldMessages();
    }
}
