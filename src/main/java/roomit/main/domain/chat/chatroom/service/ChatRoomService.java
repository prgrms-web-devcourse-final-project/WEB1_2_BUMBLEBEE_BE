package roomit.main.domain.chat.chatroom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.chat.chatroom.dto.ChatRoomResponse;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;
import roomit.main.domain.chat.chatroom.repositoroy.ChatRoomRepository;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.repository.StudyRoomRepository;
import roomit.main.global.error.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final MemberRepository memberRepository;

    public void create(Long memberId, Long studyRoomId) {
        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(ErrorCode.MEMBER_NOT_FOUND::commonException);

        Business business = studyRoom.getWorkPlace().getBusiness();

        if (chatRoomRepository.existsChatRoomByMemberIdAndBusinessId(memberId, business.getBusinessId())) {
            return;
        }

        chatRoomRepository.save(new ChatRoom(business, member));
    }

    public List<ChatRoomResponse> getRooms(CustomMemberDetails member, CustomBusinessDetails business) {
        if (member != null) {
            return chatRoomRepository.findChatRoomByMembersId(member.getId());
        }

        if (business != null) {
            return chatRoomRepository.findChatRoomByBusinessId(business.getId());
        }

        return null;
    }
}
