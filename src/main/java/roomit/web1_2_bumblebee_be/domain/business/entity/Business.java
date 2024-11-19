package roomit.web1_2_bumblebee_be.domain.business.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "business")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int businessId;

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private String businessPwd;

    @Column(nullable = false, unique = true)
    private String businessEmail;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role businessRole;

    @Column(nullable = false)
    private String businessNum;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = true)
    @LastModifiedDate
    private LocalDateTime updatedAt;

/*
    @OneToMany(mappedBy = "workPlace", cascade = CascadeType.ALL, orphanRemoval = true)
    private WorkPlace workPlace;
 */


    @Builder
    public Business(String businessName, String businessPwd, String businessEmail, Role businessRole, String businessNum) {
        this.businessName = businessName;
        this.businessPwd = businessPwd;
        this.businessEmail = businessEmail;
        this.businessRole = businessRole;
        this.businessNum = businessNum;
    }

    public void changeBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void changeBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public void changeBusinessRole(Role businessRole) {
        this.businessRole = businessRole;
    }

    public void changeBusinessNum(String businessNum) {
        this.businessNum = businessNum;
    }
}
