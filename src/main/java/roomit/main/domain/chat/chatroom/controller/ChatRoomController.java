package roomit.main.domain.chat.chatroom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.chat.chatroom.dto.response.ChatRoomResponse;
import roomit.main.domain.chat.chatroom.dto.request.ChatRoomRequest;
import roomit.main.domain.chat.chatroom.service.ChatRoomService;
import roomit.main.domain.member.dto.CustomMemberDetails;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createRoom(@RequestBody ChatRoomRequest request,
            @AuthenticationPrincipal CustomMemberDetails memberDetails){
        return chatRoomService.create(memberDetails.getId(), request.workplaceId());
    }

    @GetMapping("/room")
    @ResponseStatus(HttpStatus.OK)
    public List<? extends ChatRoomResponse> list(@AuthenticationPrincipal CustomMemberDetails memberDetails,
                                                 @AuthenticationPrincipal CustomBusinessDetails businessDetails){
        return chatRoomService.getRooms(memberDetails, businessDetails);
    }
}
