package roomit.main.domain.workplace.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.workplace.entity.value.WorkplaceAddress;
import roomit.main.domain.workplace.entity.value.WorkplaceName;
import roomit.main.domain.workplace.entity.value.WorkplacePhoneNumber;
import roomit.main.global.inner.ImageUrl;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workplace")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workplace_id", unique = true, nullable = false)
    private Long workplaceId;

    @Embedded
    private WorkplaceName workplaceName;

    @Embedded
    private WorkplacePhoneNumber workplacePhoneNumber;

    @Column(name = "workplace_description", nullable = false, length = 500)
    private String workplaceDescription;

    @Embedded
    private WorkplaceAddress workplaceAddress;

    @Column(columnDefinition = "geometry", nullable = false)
    private Point location;

    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "workplace_start_time", nullable = false)
    private LocalTime workplaceStartTime;

    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "workplace_end_time", nullable = false)
    private LocalTime workplaceEndTime;

    @Column(name = "star_sum")
    private Long starSum = 0L;

    @Column(name = "review_count")
    private Long reviewCount = 0L;

    @Embedded
    private ImageUrl imageUrl;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @OneToMany(mappedBy = "workPlace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyRoom> studyRoom = new ArrayList<>();

    @Builder
    public Workplace(final String workplaceName,
                     final String workplacePhoneNumber,
                     final String workplaceDescription,
                     final String workplaceAddress,
                     final ImageUrl imageUrl,
                     final LocalTime workplaceStartTime,
                     final LocalTime workplaceEndTime,
                     final Point location,
                     final Business business,
                     final List<StudyRoom> studyRoomList) {
        this.workplaceName = new WorkplaceName(workplaceName);
        this.workplacePhoneNumber = new WorkplacePhoneNumber(workplacePhoneNumber);
        this.workplaceDescription = workplaceDescription;
        this.workplaceAddress = new WorkplaceAddress(workplaceAddress);
        this.imageUrl = imageUrl;
        this.workplaceStartTime = workplaceStartTime;
        this.workplaceEndTime = workplaceEndTime;
        this.location = location;
        this.business = business;
        this.studyRoom = studyRoomList;
    }


    public void changeWorkplaceName(WorkplaceName workplaceName) {
        this.workplaceName = workplaceName;
    }

    public void changeWorkplacePhoneNumber(WorkplacePhoneNumber workplacePhoneNumber) {
        this.workplacePhoneNumber = workplacePhoneNumber;
    }

    public void changeWorkplaceDescription(String workplaceDescription) {
        this.workplaceDescription = workplaceDescription;
    }

    public void changeWorkplaceAddress(WorkplaceAddress workplaceAddress) {
        this.workplaceAddress = workplaceAddress;
    }

    public void changeWorkplaceStartTime(LocalTime workplaceStartTime) {
        this.workplaceStartTime = workplaceStartTime;
    }

    public void changeWorkplaceEndTime(LocalTime workplaceEndTime) {
        this.workplaceEndTime = workplaceEndTime;
    }

    public void changeStarSum(Long starSum) {
        this.starSum = starSum;
    }

    public void changeReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public void changeLocation(Point location){
        this.location = location;
    }

    public void changeImageUrl(ImageUrl imageUrl) {
        this.imageUrl = imageUrl;
    }
}

