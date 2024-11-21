package roomit.web1_2_bumblebee_be.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import roomit.web1_2_bumblebee_be.domain.review.entity.Review;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "member_nickname", nullable = false)
    private String memberNickName;

    @Column(name = "member_phonenumber", nullable = false)
    private String memberPhoneNumber;

    @Column(name = "member_age", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Age memberAge;

    @Column(name = "member_sex", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Sex memberSex;

    @Column(name = "member_email", nullable = false, unique = true)
    @Email
    private String memberEmail;

    @Column(name = "member_pwd", nullable = false)
    private String memberPwd;

    @Column(name = "member_role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role memberRole;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime deleteAt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList();
//
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Reservation> reservations = new ArrayList();

    @Builder
    public Member(String memberNickName, String memberPhoneNumber, Age memberAge, Sex memberSex, String memberEmail, String memberPwd, Role memberRole ,Review review /*Reservation reservation*/) {
        this.memberNickName = memberNickName;
        this.memberPhoneNumber = memberPhoneNumber;
        this.memberAge = memberAge;
        this.memberSex = memberSex;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberRole = Role.ROLE_USER;
//        this.reviews = (List<Review>) review;
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
