//package roomit.web1_2_bumblebee_be.global.controller;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import roomit.web1_2_bumblebee_be.domain.member.entity.Age;
//import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
//import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
//import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;
//import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
//
//import java.util.Random;
//import java.util.stream.IntStream;
//
//@Component
//public class DummyDataInitializer implements CommandLineRunner {
//
//    private final MemberRepository memberRepository;
//
//    public DummyDataInitializer(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//
//    @Override
//    public void run(String... args) {
//        Random random = new Random();
//        String[] nicknames = {"UserA", "UserB", "UserC", "UserD", "UserE"};
//        String[] phonePrefixes = {"010-1234", "010-5678", "010-9876"};
//        String[] domains = {"example.com", "dummy.com", "test.com"};
//
//        IntStream.range(1, 51).forEach(i -> {
//            // 빌더 패턴을 이용하여 Member 객체 생성
//            Member member = Member.builder()
//                    .memberNickName(nicknames[random.nextInt(nicknames.length)] + i)
//                    .memberPhoneNumber(phonePrefixes[random.nextInt(phonePrefixes.length)] + i)
//                    .memberAge(Age.values()[random.nextInt(Age.values().length)])
//                    .memberSex(Sex.values()[random.nextInt(Sex.values().length)])
//                    .memberEmail("user" + i + "@" + domains[random.nextInt(domains.length)])
//                    .memberPwd("password" + i)
//                    .memberRole(Role.ROLE_ADMIN) // 기본 역할 고정
//                    .build();
//
//            memberRepository.save(member);
//        });
//    }
//}