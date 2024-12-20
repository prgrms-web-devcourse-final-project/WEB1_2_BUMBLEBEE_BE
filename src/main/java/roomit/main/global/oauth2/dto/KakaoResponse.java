package roomit.main.global.oauth2.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private Map<String, Object> attributes;
    private Map<String, Object> kakaoAccountAttributes;
    private Map<String, Object> profileAttributes;

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
        this.profileAttributes = (Map<String, Object>) kakaoAccountAttributes.get("profile");

    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public Provider getProvider() {
        return Provider.KAKAO;
    }

    @Override
    public String getNickname() {
        return profileAttributes.get("nickname").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccountAttributes.get("email").toString();
    }

}
