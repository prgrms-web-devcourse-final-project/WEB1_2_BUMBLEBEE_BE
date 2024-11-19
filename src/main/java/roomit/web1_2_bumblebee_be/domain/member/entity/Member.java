package roomit.web1_2_bumblebee_be.domain.member.entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private Long id;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private Sex sex;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private Role role;

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
    public Member(String nickName, String phoneNumber, int age, Sex sex, String email, String pwd, Role role /*Review review, Reservation reservation*/) {
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.sex = sex;
        this.email = email;
        this.pwd = pwd;
        this.role = role;
        this.createdAt = LocalDateTime.now();
//        this.review = review;
//        this.reservation = reservation;
    }

    public void changePwd(String newPwd) {
        this.pwd = newPwd;
    }
    public void changeRole(Role newRole) {
        this.role = newRole;
    }
    public void changeEmail(String newEmail) {
        this.email = newEmail;
    }
    public void changePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }
    public void changeAge(int newAge) {
        this.age = newAge;
    }
    public void nickName(String newNickName) {
        this.nickName = newNickName;
    }
}
