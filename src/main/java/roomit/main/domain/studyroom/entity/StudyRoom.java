package roomit.main.domain.studyroom.entity;

import jakarta.persistence.*;
import lombok.*;
import roomit.main.domain.studyroom.entity.value.BaseEntity;
import roomit.main.domain.studyroom.entity.value.StudyRoomName;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.global.inner.ImageUrl;

@Entity
@Table(name = "StudyRoom")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @Builder
    public StudyRoom(String studyRoomName, String description, Integer capacity, Integer price,ImageUrl imageUrl,Workplace workplace) {
        this.studyRoomName = new StudyRoomName(studyRoomName);
        this.description = description;
        this.capacity = capacity;
        this.price = price;
        this.imageUrl = imageUrl;
        this.workPlace = workplace;
    }

}