package roomit.main.domain.chat.chatroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor
//@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public ChatRoom(Business business, Member member, LocalDateTime createdAt) {
        this.business = business;
        this.member = member;
        this.createdAt = createdAt;
    }
}