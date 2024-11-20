package roomit.web1_2_bumblebee_be.domain.studyroom.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "studyroom_tbl")
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Getter
public class StudyroomEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studyroom_ID", unique = true, updatable = false, columnDefinition = "BIGINT")
    private Long studyroomId;

    @Column(name = "studyroom_title", nullable = false , columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(name = "studyroom_description", nullable = false ,columnDefinition = "VARCHAR(255)")
    private String description;

    @Column(name = "studyroom_location", nullable = false, columnDefinition = "VARCHAR(255)")
    private String location;

    @Column(name = "studyroom_num", nullable = false, columnDefinition = "BIGINT")
    private Integer num;

    /* @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "workplace_id", updatable = false,columnDefinition = "BIGINT")
    private WorkPlace workPlace; */
}