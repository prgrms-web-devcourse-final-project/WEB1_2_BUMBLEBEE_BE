package roomit.main.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.workplace.entity.Workplace;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Column(name = "review_content", nullable = false, length = 50)
    private String reviewContent;

    @Column(name = "review_rating", nullable = false)
    private Double reviewRating;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updatedAt;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workplace_id", nullable = false)
    private Workplace workplace;

    @Builder
    public Review(String reviewContent, Double reviewRating, Member member, Workplace workplace) {
        this.reviewContent = reviewContent;
        this.reviewRating = reviewRating;
        this.createdAt = LocalDateTime.now();
        this.member = member;
        this.workplace = workplace;
    }

    public void changeReviewRating(Double reviewRating) {
        this.reviewRating = reviewRating;
    }
    public void changeReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
