package roomit.main.domain.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.member.entity.Age;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.entity.Role;
import roomit.main.domain.member.entity.Sex;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.oauth2.dto.KakaoResponse;
import roomit.main.domain.oauth2.dto.NaverResponse;
import roomit.main.domain.oauth2.dto.OAuth2Response;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")) {

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        String provider = username.substring(0,5)+oAuth2Response.getNickname();
        Member existData = memberRepository.findByMemberNickName(provider);


        if (existData == null){
            Member data = Member.builder()
                    .memberRole(Role.ROLE_USER)
                    .memberSex(Sex.MALE)
                    .memberPhoneNumber("010-1212-3232")
                    .memberPwd("TestPwd12!")
                    .memberEmail(oAuth2Response.getEmail())
                    .memberNickName(provider)
                    .passwordEncoder(new BCryptPasswordEncoder())
                    .build();

            Member member = memberRepository.save(data);

            return new CustomMemberDetails(member);
        } else {
            existData.changeEmail(oAuth2Response.getEmail());

            Member member = memberRepository.save(existData);

            return new CustomMemberDetails(member);
        }
    }
}
