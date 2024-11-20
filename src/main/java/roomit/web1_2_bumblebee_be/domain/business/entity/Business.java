package roomit.web1_2_bumblebee_be.domain.business.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "business")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_id")
    private int businessId;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "business_pwd",nullable = false)
    private String businessPwd;

    @Column(name = "business_email",nullable = false, unique = true)
    private String businessEmail;

    @Column(name = "business_role",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role businessRole;

    @Column(name = "business_num",nullable = false)
    private String businessNum;

    @Column(name = "createdAt",nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = true)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workplace> workplace;



    @Builder
    public Business(String businessName, String businessPwd, String businessEmail,String businessNum) {
        this.businessName = businessName;
        this.businessPwd = businessPwd;
        this.businessEmail = businessEmail;
        this.businessRole = Role.Business;
        this.businessNum = businessNum;
    }

    public void changeBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void changeBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public void changeBusinessNum(String businessNum) {
        this.businessNum = businessNum;
    }
}
