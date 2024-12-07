package roomit.main.domain.chat.chatmessage.entity;

import roomit.main.global.error.ErrorCode;

public enum SenderType {
    BUSINESS, MEMBER;


    public static SenderType fromString(String type) {
        return SenderType.valueOf(type.toUpperCase()); // 대소문자 무관 변환
    }
}
