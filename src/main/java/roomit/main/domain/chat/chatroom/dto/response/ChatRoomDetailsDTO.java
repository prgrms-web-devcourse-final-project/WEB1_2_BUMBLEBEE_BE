package roomit.main.domain.chat.chatroom.dto.response;

public record ChatRoomDetailsDTO(
        Long roomId,
        String memberNickName,
        String businessName
) {
    public ChatRoomDetailsDTO(Long roomId, String memberNickName, String businessName) {
        this.roomId = roomId;
        this.memberNickName = memberNickName;
        this.businessName = businessName;
    }
}
