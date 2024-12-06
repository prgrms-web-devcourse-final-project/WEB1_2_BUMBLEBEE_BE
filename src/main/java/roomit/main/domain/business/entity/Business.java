package roomit.main.domain.business.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import roomit.main.domain.business.entity.value.BusinessEmail;
import roomit.main.domain.business.entity.value.BusinessNickname;
import roomit.main.domain.business.entity.value.BusinessNum;
import roomit.main.domain.business.entity.value.BusinessPassword;
import roomit.main.domain.business.dto.request.BusinessUpdateRequest;
import roomit.main.domain.member.entity.Role;
import roomit.main.domain.workplace.entity.Workplace;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "business", indexes = {
        @Index(name = "idx_businessId", columnList = "business_id")})
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_id")
    private Long businessId;

    @Embedded
    private BusinessNickname businessName;

    @Embedded
    private BusinessPassword businessPwd;

    @Embedded
    private BusinessEmail businessEmail;

    @Embedded
    private BusinessNum businessNum;

    @Column(name = "business_role",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role businessRole;

    @Column(name = "createdAt",nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workplace> workplace;

    @Builder
    public Business(final String businessName,
                    final String businessPwd,
                    final String businessEmail,
                    final String businessNum,
                    final PasswordEncoder passwordEncoder) {
        this.businessName = new BusinessNickname(businessName);
        this.businessPwd = new BusinessPassword(businessPwd, passwordEncoder);
        this.businessEmail = new BusinessEmail(businessEmail);
        this.businessNum = new BusinessNum(businessNum);
        this.businessRole = Role.ROLE_BUSINESS;
    }

    public String getBusinessName() {
        return this.businessName.getValue();
    }

    public String getBusinessEmail() {
        return this.businessEmail.getValue();
    }

    public String getBusinessNum() {
        return this.businessNum.getValue();
    }


    public void updateBusiness(final BusinessUpdateRequest businessUpdateRequest) {
        this.businessName = new BusinessNickname(businessUpdateRequest.businessName());
        this.businessEmail = new BusinessEmail(businessUpdateRequest.businessEmail());
        this.businessNum = new BusinessNum(businessUpdateRequest.businessNum());
    }

}
