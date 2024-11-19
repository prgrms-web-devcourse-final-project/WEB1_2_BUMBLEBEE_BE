package roomit.web1_2_bumblebee_be.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String memberNickName;

    @Column(nullable = false)
    private String memberPhoneNumber;

    @Column(nullable = false)
    private int memberAge;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Sex memberSex;

    @Column(nullable = false, unique = true)
    @Email
    private String memberEmail;

    @Column(nullable = false)
    private String memberPwd;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role memberRole;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime deleteAt;

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Review> reviews = new ArrayList();
//
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Reservation> reservations = new ArrayList();

    @Builder
    public Member(String memberNickName, String memberPhoneNumber, int memberAge, Sex memberSex, String memberEmail, String memberPwd, Role memberRole /*Review review, Reservation reservation*/) {
        this.memberNickName = memberNickName;
        this.memberPhoneNumber = memberPhoneNumber;
        this.memberAge = memberAge;
        this.memberSex = memberSex;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberRole = memberRole;
//        this.review = review;
//        this.reservation = reservation;
    }

    public void changePwd(String newPwd) {
        this.memberPwd = newPwd;
    }
    public void changeEmail(String newEmail) {
        this.memberEmail = newEmail;
    }
    public void changePhoneNumber(String newPhoneNumber) {
        this.memberPhoneNumber = newPhoneNumber;
    }
    public void changeNickName(String newNickName) {
        this.memberNickName = newNickName;
    }
}
