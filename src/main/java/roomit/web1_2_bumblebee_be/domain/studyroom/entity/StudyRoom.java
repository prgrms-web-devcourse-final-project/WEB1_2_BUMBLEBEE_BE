package roomit.web1_2_bumblebee_be.domain.studyroom.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "StudyRoom")
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Getter
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
    private Integer num;

    @Column(name = "studyroom_price", nullable = false,columnDefinition = "INT")
    private Integer price;

    /* @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "workplace_id", updatable = false,columnDefinition = "BIGINT")
    private WorkPlace workPlace;*/
}