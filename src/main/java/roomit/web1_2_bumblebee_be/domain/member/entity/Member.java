package roomit.web1_2_bumblebee_be.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import roomit.web1_2_bumblebee_be.domain.member.entity.value.MemberEmail;
import roomit.web1_2_bumblebee_be.domain.member.entity.value.MemberNickname;
import roomit.web1_2_bumblebee_be.domain.member.entity.value.MemberPassword;
import roomit.web1_2_bumblebee_be.domain.member.entity.value.MemberPhoneNumber;
import roomit.web1_2_bumblebee_be.domain.review.entity.Review;

import java.time.LocalDate;
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

    @Embedded
    private MemberNickname memberNickname;

    @Embedded
    private MemberPhoneNumber memberPhonenumber;

    @Embedded
    private MemberEmail memberEmail;

    @Embedded
    private MemberPassword memberPwd;

    @Column(name = "member_sex", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Sex memberSex;

    @Column(name = "member_role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role memberRole;

    @Column(name = "birth_day", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDay;

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
    public Member(String memberNickName, String memberPhoneNumber, LocalDate birthDay, Sex memberSex, String memberEmail, String memberPwd, Role memberRole , PasswordEncoder passwordEncoder /*Reservation reservation*/) {
        this.memberNickname = new MemberNickname(memberNickName);
        this.memberPhonenumber = new MemberPhoneNumber(memberPhoneNumber);
        this.memberEmail = new MemberEmail(memberEmail);
        this.memberPwd = new MemberPassword(memberPwd, passwordEncoder);
        this.birthDay = birthDay;
        this.memberSex = memberSex;
        this.memberRole = Role.ROLE_USER;
//        this.reservation = reservation;
    }

    public String getMemberNickName(){
        return this.memberNickname.getValue();
    }
    public String getMemberPhoneNumber(){
        return this.memberPhonenumber.getValue();
    }
    public String getMemberEmail(){
        return this.memberEmail.getValue();
    }
    public String getMemberPwd(){
        return this.memberPwd.getValue();
    }

    public void changeEmail(String newEmail) {
        this.memberEmail = new MemberEmail(newEmail);
    }
    public void changePhoneNumber(String newPhoneNumber) {
        this.memberPhonenumber = new MemberPhoneNumber(newPhoneNumber);
    }
    public void changeNickName(String newNickName) {
        this.memberNickname = new MemberNickname(newNickName);
    }
    public void changePwd(String newPwd) {
        this.memberPwd = new MemberPassword(newPwd, new BCryptPasswordEncoder());
    }
}
