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
        m.room.roomId, 
        m.room.business.businessId, 
        CASE 
            WHEN MAX(m.timestamp) IS NOT NULL THEN MAX(m.timestamp) 
            ELSE c.createdAt 
        END
    ) 
    FROM ChatRoom c
    LEFT JOIN c.messages m
    WHERE m.room.member.memberId = :memberId
    GROUP BY m.room.roomId, m.room.business, c.createdAt
    ORDER BY 
        CASE 
            WHEN MAX(m.timestamp) IS NOT NULL THEN MAX(m.timestamp) 
            ELSE c.createdAt 
        END DESC
    """)
    List<ChatRoomResponse> findChatRoomByMembersId(Long memberId);

    @Query("""
    SELECT new roomit.main.domain.chat.chatroom.dto.ChatRoomBusinessResponse(
        m.room.roomId, 
        m.room.member.memberId, 
        CASE 
            WHEN MAX(m.timestamp) IS NOT NULL THEN MAX(m.timestamp) 
            ELSE c.createdAt 
        END
    ) 
    FROM ChatRoom c
    LEFT JOIN c.messages m
    WHERE m.room.business.businessId = :businessId
    GROUP BY m.room.roomId, m.room.member, c.createdAt
    ORDER BY 
        CASE 
            WHEN MAX(m.timestamp) IS NOT NULL THEN MAX(m.timestamp) 
            ELSE c.createdAt 
        END DESC
    """)
    List<ChatRoomResponse> findChatRoomByBusinessId(Long businessId);

    @Query("""
    SELECT m.roomId
    FROM ChatRoom m
    where m.member.memberId = :memberId and m.business.businessId = :businessId
""")
    Long findChatRoomId(Long memberId, Long businessId);

}