package roomit.main.domain.chat.chatroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.chat.chatmessage.entity.ChatMessage;
import roomit.main.domain.chat.chatmessage.entity.SenderType;
import roomit.main.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor
//@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Long workplaceId;

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    public ChatRoom(Business business, Member member, Long workplaceId) {
        this.business = business;
        this.member = member;
        this.workplaceId = workplaceId;
    }

    public boolean isSenderValid(SenderType senderType, String sender) {
        return (senderType.equals(SenderType.BUSINESS) && business.getBusinessName().equals(sender))
               || (senderType.equals(SenderType.MEMBER) && member.getMemberNickName().equals(sender));

    }

}