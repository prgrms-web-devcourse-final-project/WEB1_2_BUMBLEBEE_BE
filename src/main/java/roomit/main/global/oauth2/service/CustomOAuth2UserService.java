package roomit.main.global.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.entity.Role;
import roomit.main.domain.member.entity.Sex;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.global.oauth2.dto.KakaoResponse;
import roomit.main.global.oauth2.dto.NaverResponse;
import roomit.main.global.oauth2.dto.OAuth2Response;
import roomit.main.global.oauth2.dto.Provider;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

            OAuth2User oAuth2User = super.loadUser(userRequest);

            String registrationId = userRequest.getClientRegistration().getRegistrationId();

            OAuth2Response oAuth2Response = switch (Provider.valueOf(registrationId.toUpperCase())) {
                case NAVER -> new NaverResponse(oAuth2User.getAttributes());
                case KAKAO -> new KakaoResponse(oAuth2User.getAttributes());
                default -> null;
            };

            Provider provider = oAuth2Response.getProvider(); //NAVER,KAKAO
            String nickname = oAuth2Response.getNickname(); //닉네임 = 이중호,홍길동 등등
            Member existData = memberRepository.findByMemberNickNameAndProvider(nickname, provider);
        if (existData == null){
            Member data = Member.builder()
                    .memberRole(Role.ROLE_USER)
                    .memberSex(Sex.MALE)
                    .memberPhoneNumber("010-1212-3232")
                    .memberPwd("TestPwd12!")
                    .memberEmail(oAuth2Response.getEmail())
                    .memberNickName(nickname)
                    .provider(provider)
                    .passwordEncoder(new BCryptPasswordEncoder())
                    .build();

            Member member = memberRepository.save(data);

            return new CustomMemberDetails(member);
        } else {

            return new CustomMemberDetails(existData);
        }
    }
}
