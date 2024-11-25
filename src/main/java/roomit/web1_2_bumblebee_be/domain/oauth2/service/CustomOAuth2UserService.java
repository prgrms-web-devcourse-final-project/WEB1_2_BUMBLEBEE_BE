package roomit.web1_2_bumblebee_be.domain.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.member.dto.CustomMemberDetails;
import roomit.web1_2_bumblebee_be.domain.member.entity.Age;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;
import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
import roomit.web1_2_bumblebee_be.domain.oauth2.dto.*;

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
        Member existData = memberRepository.findByMemberNickName(username);

        if (existData == null){
            //username == nickname
            Member data = Member.builder()
                    .memberRole(Role.ROLE_USER)
                    .memberAge(Age.TWENTY)
                    .memberSex(Sex.MALE)
                    .memberPhoneNumber("010-1111-2222")
                    .memberPwd("1111")
                    .memberEmail(oAuth2Response.getEmail())
                    .memberNickName(username)
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
