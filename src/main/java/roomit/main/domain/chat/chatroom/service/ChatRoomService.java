package roomit.main.domain.chat.chatroom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.chat.chatmessage.entity.ChatMessage;
import roomit.main.domain.chat.chatroom.dto.response.ChatRoomBusinessResponse;
import roomit.main.domain.chat.chatroom.dto.response.ChatRoomMemberResponse;
import roomit.main.domain.chat.chatroom.dto.response.ChatRoomResponse;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;
import roomit.main.domain.chat.chatroom.repository.ChatRoomRepository;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final WorkplaceRepository workplaceRepository;
    private final MemberRepository memberRepository;

    public Long create(Long memberId, Long workplaceId) {
        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(ErrorCode.MEMBER_NOT_FOUND::commonException);

        Business business = workplace.getBusiness();

        if (!chatRoomRepository.existsChatRoomByMemberIdAndBusinessId(memberId, business.getBusinessId())) {
            chatRoomRepository.save(new ChatRoom(business, member, workplace.getWorkplaceName().getValue()));
        }

        return chatRoomRepository.findChatRoomId(memberId, business.getBusinessId());
    }

    public List<? extends ChatRoomResponse> getRooms(CustomMemberDetails member, CustomBusinessDetails business) {
            if (member != null) {
                List<Object[]> chatRooms = chatRoomRepository.findChatRoomByMembersId(member.getId());

                return chatRooms.stream()
                        .map(result -> {
                            ChatRoom chatRoom = (ChatRoom) result[0];
                            ChatMessage chatMessage = result[1] != null ? (ChatMessage) result[1] : null;

                            // Workplace 이름 가져오기
                            String workplaceName = chatRoom.getWorkplaceName();


                            return new ChatRoomMemberResponse(chatRoom, chatMessage, workplaceName);
                        })
                        .toList();
            }

        if (business != null) {
            List<Object[]> chatRooms = chatRoomRepository.findChatRoomByBusinessId(business.getId());

            return chatRooms.stream()
                    .map(result -> {
                        ChatRoom chatRoom = (ChatRoom) result[0];
                        ChatMessage chatMessage = result[1] != null ? (ChatMessage) result[1] : null;

                        return new ChatRoomBusinessResponse(chatRoom, chatMessage);
                    })
                    .toList();
        }

        return Collections.emptyList();
    }
}
