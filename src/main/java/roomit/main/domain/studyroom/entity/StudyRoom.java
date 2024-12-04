package roomit.main.domain.studyroom.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.studyroom.dto.request.UpdateStudyRoomRequest;
import roomit.main.domain.studyroom.entity.value.BaseEntity;
import roomit.main.domain.studyroom.entity.value.StudyRoomName;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.global.inner.ImageUrl;

@Entity
@Table(name = "studyroom")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudyRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studyroom_id")
    private Long studyRoomId;

    @Embedded
    private StudyRoomName studyRoomName;

    @Column(name = "studyroom_description", nullable = false)
    private String description;

    @Column(name = "studyroom_capacity", nullable = false)
    private Integer capacity;

    @Column(name = "studyroom_price", nullable = false)
    private Integer price;

    @Embedded
    private ImageUrl imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workplace_id", nullable = false)
    private Workplace workPlace;

    @OneToMany(mappedBy = "studyRoom", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reservation> reservations;

    @Builder
    public StudyRoom(String studyRoomName, String description, Integer capacity, Integer price,
        ImageUrl imageUrl, Workplace workplace) {
        this.studyRoomName = new StudyRoomName(studyRoomName);
        this.description = description;
        this.capacity = capacity;
        this.price = price;
        this.imageUrl = imageUrl;
        this.workPlace = workplace;
    }

    public void updatedStudyRoom(final UpdateStudyRoomRequest updateStudyRoomRequest) {
        this.studyRoomName = new StudyRoomName(updateStudyRoomRequest.studyRoomName());
        this.description = updateStudyRoomRequest.description();
        this.capacity = updateStudyRoomRequest.capacity();
        this.price = updateStudyRoomRequest.price();
    }
}