package roomit.main.domain.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import roomit.main.domain.reservation.entity.Reservation;

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

    @Column(name = "review_content", nullable = false, length = 100)
    private String reviewContent;

    @Column(name = "review_rating", nullable = false)
    private Integer reviewRating;

    @Column(name = "workplace_name", nullable = false)
    private String workplaceName;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = true) // 리뷰는 없어도 됨
    private Reservation reservation;

    @Builder
    public Review(String reviewContent, Integer reviewRating, String workplaceName, Reservation reservation) {
        this.reviewContent = reviewContent;
        this.reviewRating = reviewRating;
        this.workplaceName = workplaceName;
        this.createdAt = LocalDateTime.now();
        this.reservation = reservation;
    }

    public void changeReviewRating(Integer reviewRating) {
        this.reviewRating = reviewRating;
    }
    public void changeReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
    public void changeReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public boolean checkMyReservation(Reservation reservation , Long memberId) {
       return !Objects.equals(reservation.getMember().getMemberId(), memberId);
    }
}
