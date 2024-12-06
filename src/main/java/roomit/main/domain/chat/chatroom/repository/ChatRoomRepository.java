package roomit.main.domain.chat.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("""
        SELECT c
        FROM ChatRoom c
        left join fetch c.business b
        left join fetch c.member m
        WHERE c.roomId = :roomId
    """)
    Optional<ChatRoom> findRoomDetailsById(@Param("roomId") Long roomId);


    @Query("SELECT c.roomId FROM ChatRoom c")
    List<Long> findAllRoomIds();

    @Query("""
        SELECT CASE WHEN EXISTS (
            SELECT 1
            FROM ChatRoom c
            WHERE c.member.memberId = :memberId AND c.business.businessId = :businessId
            ) THEN TRUE ELSE FALSE END
    """)
    Boolean existsChatRoomByMemberIdAndBusinessId(Long memberId, Long businessId);

    @Query("""
        SELECT c, m
        FROM ChatRoom c
        LEFT JOIN c.messages m
        WHERE c.member.memberId = :memberId
            AND (m.timestamp = (
            SELECT MAX(m2.timestamp)
            FROM ChatMessage m2
            WHERE m2.room.roomId = c.roomId
            ) OR m.timestamp IS NULL)
        ORDER BY COALESCE(m.timestamp, c.createdAt) DESC
    """)
    List<Object[]> findChatRoomByMembersId(Long memberId);


    @Query("""
        SELECT c, msg
        FROM ChatRoom c
        LEFT JOIN c.member m
        LEFT JOIN FETCH c.messages msg
        WHERE c.business.businessId = :businessId
        AND (msg.timestamp = (
            SELECT MAX(m2.timestamp)
            FROM ChatMessage m2
            WHERE m2.room.roomId = c.roomId
        ) OR msg.timestamp IS NULL)
        ORDER BY COALESCE(msg.timestamp, c.createdAt) DESC
    """)
    List<Object[]> findChatRoomByBusinessId(Long businessId);

    @Query("""
        SELECT m.roomId
        FROM ChatRoom m
        where m.member.memberId = :memberId and m.business.businessId = :businessId
    """)
    Long findChatRoomId(Long memberId, Long businessId);

}