package roomit.main.domain.chat.chatmessage.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import roomit.main.domain.chat.chatmessage.entity.SenderType;

import java.time.LocalDateTime;
import java.util.List;

public record ChatMessageSaveRequest (
        Long roomId,
        String sender,
        String content,
        LocalDateTime timestamp,
        SenderType senderType,
        Boolean isRead
) {
    public static ChatMessageSaveRequest fromRedis(Object value, ObjectMapper objectMapper) {
        ChatMessageSaveRequest request = objectMapper.convertValue(value, ChatMessageSaveRequest.class);

        // timestamp 변환 (필요할 경우)
        LocalDateTime parsedTimestamp = convertTimestamp(request.timestamp());
        return new ChatMessageSaveRequest(
                request.roomId(),
                request.sender(),
                request.content(),
                parsedTimestamp,
                request.senderType(),
                false
        );
    }

    // timestamp 변환 로직
    private static LocalDateTime convertTimestamp(Object timestamp) {
        if (timestamp instanceof List<?> timestampArray && timestampArray.size() >= 6) {
            int[] dateTimeArray = timestampArray.stream().mapToInt(o -> ((Number) o).intValue()).toArray();
            return LocalDateTime.of(
                    dateTimeArray[0], dateTimeArray[1], dateTimeArray[2],
                    dateTimeArray[3], dateTimeArray[4], dateTimeArray[5],
                    dateTimeArray.length > 6 ? dateTimeArray[6] : 0
            );
        } else if (timestamp instanceof String stringTimestamp) {
            return LocalDateTime.parse(stringTimestamp); // 문자열 형식도 지원
        }
        throw new IllegalArgumentException("Invalid timestamp format: " + timestamp);
    }

    public ChatMessageSaveRequest(ChatMessageRequest request) {
        this(
                request.roomId(),
                request.sender(),
                request.content(),
                request.timestamp(),
                request.getSenderTypeEnum(),
                false
        );
    }

    // 새로운 ChatMessageSaveRequest를 생성하며, 문자열로 변환된 타임스탬프를 포함
    public ChatMessageSaveRequest withFormattedTimestamp() {
        return new ChatMessageSaveRequest(
                this.roomId,
                this.sender,
                this.content,
                LocalDateTime.parse(this.timestamp.toString()),
                this.senderType,
                this.isRead
        );
    }

    // 읽음 상태를 변경한 새로운 객체 생성
    public ChatMessageSaveRequest withRead(boolean isRead) {
        return new ChatMessageSaveRequest(
                this.roomId,
                this.sender,
                this.content,
                this.timestamp,
                this.senderType,
                isRead
        );
    }

}