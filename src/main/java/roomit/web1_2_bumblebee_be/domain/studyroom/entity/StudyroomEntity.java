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
    private Long id;

    @NotNull
    @Column(name = "studyroom_title", columnDefinition = "VARCHAR(255)")
    private String title;

    @NotNull
    @Column(name = "studyroom_description",  columnDefinition = "VARCHAR(255)")
    private String description;

    @NotNull
    @Column(name = "studyroom_location", columnDefinition = "VARCHAR(255)")
    private String location;

    @NotNull
    @Column(name = "studyroom_num", columnDefinition = "BIGINT")
    private Long num;
}