package roomit.main.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.reservation.entity.Reservation;
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
    private int reviewRating;

    @Column(name = "workplace_name", nullable = false)
    private String workplaceName;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "review") // Reservation에서 매핑 관리
    private Reservation reservation;

    @Builder
    public Review(String reviewContent, int reviewRating, String workplaceName, Reservation reservation) {
        this.reviewContent = reviewContent;
        this.reviewRating = reviewRating;
        this.workplaceName = workplaceName;
        this.createdAt = LocalDateTime.now();
        this.reservation = reservation;
    }

    public void changeReviewRating(int reviewRating) {
        this.reviewRating = reviewRating;
    }
    public void changeReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
