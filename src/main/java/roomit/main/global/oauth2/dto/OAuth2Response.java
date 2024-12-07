package roomit.main.global.oauth2.dto;

public interface OAuth2Response {
    //제공자 (네이버인지,카카오인지)
    Provider getProvider();

    //제공자에서 발급해주는 아이디(번호)
    String getProviderId();

    //이메일
    String getEmail();

    //사용자 별명
    String getNickname();



}
