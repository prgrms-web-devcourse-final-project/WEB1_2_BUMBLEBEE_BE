package roomit.web1_2_bumblebee_be.domain.workplace.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import roomit.web1_2_bumblebee_be.domain.business.entity.Business;

import java.time.LocalDateTime;

@Entity
@Table(name = "workplace")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="workplace_id", unique = true, nullable = false)
    private Long workplaceId;

    @Column(name = "workplace_name", nullable = false, length = 100)
    private String workplaceName;

    @Column(name = "workplace_phone_number", nullable = false, length = 15)
    private String workplacePhoneNumber;

    @Column(name = "workplace_description", nullable = false)
    private String workplaceDescription;

    @Column(name = "workplace_address", nullable = false, length = 255)
    private String workplaceAddress;

    @Column(name = "workplace_start_time", nullable = false)
    private LocalDateTime workplaceStartTime;

    @Column(name = "workplace_end_time", nullable = false)
    private LocalDateTime workplaceEndTime;

    @Column(name = "star_sum")
    private Long starSum;

    @Lob
    @Column(name = "profile_image")
    private byte[] profileImage;

    @Column(name = "image_type", length = 50)
    private String imageType;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

//    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    private List<Room> rooms = new ArrayList<>();

    @Builder
    public Workplace(Long workplaceId, String workplaceName, String workplacePhoneNumber, String workplaceDescription, String workplaceAddress, LocalDateTime workplaceStartTime, LocalDateTime workplaceEndTime, Long starSum, byte[] profileImage, String imageType, LocalDateTime createdAt, LocalDateTime updatedAt, Business business) {
        this.workplaceId = workplaceId;
        this.workplaceName = workplaceName;
        this.workplacePhoneNumber = workplacePhoneNumber;
        this.workplaceDescription = workplaceDescription;
        this.workplaceAddress = workplaceAddress;
        this.workplaceStartTime = workplaceStartTime;
        this.workplaceEndTime = workplaceEndTime;
        this.business = business;
        this.starSum = starSum;
        this.profileImage = profileImage;
        this.imageType = imageType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public void changeWorkplaceName(String workplaceName) {
        this.workplaceName = workplaceName;
    }

    public void changeWorkplacePhoneNumber(String workplacePhoneNumber) {
        this.workplacePhoneNumber = workplacePhoneNumber;
    }

    public void changeWorkplaceDescription(String workplaceDescription) {
        this.workplaceDescription = workplaceDescription;
    }

    public void changeWorkplaceAddress(String workplaceAddress) {
        this.workplaceAddress = workplaceAddress;
    }

    public void changeWorkplaceStartTime(LocalDateTime workplaceStartTime) {
        this.workplaceStartTime = workplaceStartTime;
    }

    public void changeWorkplaceEndTime(LocalDateTime workplaceEndTime) {
        this.workplaceEndTime = workplaceEndTime;
    }

    public void changeStarSum(Long starSum) {
        this.starSum = starSum;
    }

    public void changeProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public void changeImageType(String imageType) {
        this.imageType = imageType;
    }
}
