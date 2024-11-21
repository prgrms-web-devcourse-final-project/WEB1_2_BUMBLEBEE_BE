package roomit.web1_2_bumblebee_be.domain.studyroom.entity;

import jakarta.persistence.*;
import lombok.*;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;

@Entity
@Table(name = "StudyRoom")
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class StudyRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studyroom_id", unique = true, updatable = false, columnDefinition = "BIGINT")
    private Long studyroomId;

    @Column(name = "studyroom_title", nullable = false , columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(name = "studyroom_description", nullable = false ,columnDefinition = "VARCHAR(255)")
    private String description;

    @Column(name = "studyroom_num", nullable = false, columnDefinition = "INT")
    private Integer capacity;

    @Column(name = "studyroom_price", nullable = false,columnDefinition = "INT")
    private Integer price;

    @Column(name = "studyroom_image_url", nullable = false, columnDefinition = "VARCHAR(255)")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workplace_id", nullable = false)
    private Workplace workPlaceId;

    @Builder
    public StudyRoom(String title, String description, Integer capacity, Integer price,String imageUrl,Workplace workplaceId) {
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.price = price;
        this.imageUrl = imageUrl;
        this.workPlaceId = workplaceId;
    }

    public static class StudyRoomBuilder {
        public StudyRoomBuilder workPlaceId(Workplace workPlaceId) {
            this.workplaceId = workPlaceId;
            return this;
        }
    }
}