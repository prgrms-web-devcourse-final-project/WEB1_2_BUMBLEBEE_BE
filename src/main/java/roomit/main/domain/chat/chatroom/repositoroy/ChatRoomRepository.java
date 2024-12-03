package roomit.main.domain.chat.chatroom.repositoroy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import roomit.main.domain.chat.chatroom.dto.ChatRoomDetailsDTO;
import roomit.main.domain.chat.chatroom.dto.ChatRoomResponse;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("""
            SELECT new roomit.main.domain.chat.chatroom.dto.ChatRoomDetailsDTO(
            c.roomId, c.member.memberNickname.value, c.business.businessName.value
            )
            FROM ChatRoom c 
            WHERE c.roomId = :roomId
        """)
    Optional<ChatRoomDetailsDTO> findRoomDetailsById(@Param("roomId") Long roomId);


    @Query("SELECT c.roomId FROM ChatRoom c")
    List<Long> findAllRoomIds();

    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END
        FROM ChatRoom c 
        where c.member.memberId = :memberId and c.business.businessId = :businessId
    """)
    Boolean existsChatRoomByMemberIdAndBusinessId(Long memberId, Long businessId);

    @Query("""
        SELECT new roomit.main.domain.chat.chatroom.dto.ChatRoomMemberResponse(
            m.room.roomId, m.room.business.businessId, MAX(m.timestamp)
        ) 
        FROM ChatMessage m
        where m.room.member.memberId = :memberId
        GROUP BY m.room.roomId, m.room.business
        ORDER BY max(m.timestamp) DESC
    """)
    List<ChatRoomResponse> findChatRoomByMembersId(Long memberId);

    @Query("""
        SELECT new roomit.main.domain.chat.chatroom.dto.ChatRoomBusinessResponse(
            m.room.roomId, m.room.member.memberId, MAX(m.timestamp)
        ) 
        FROM ChatMessage m
        where m.room.business.businessId = :businessId
        GROUP BY m.room.roomId, m.room.member
        ORDER BY max(m.timestamp) DESC
    """)
    List<ChatRoomResponse> findChatRoomByBusinessId(Long businessId);

}