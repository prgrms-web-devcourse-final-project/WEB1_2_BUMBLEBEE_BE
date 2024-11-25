package roomit.main.domain.member.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import roomit.main.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomMemberDetails implements UserDetails, OAuth2User {

    private final Member member;

    private Map<String, Object> attributes;

    // 일반 로그인
    public CustomMemberDetails(Member member){
        this.member=member;
    }
    // OAuth 로그인
    public CustomMemberDetails(Member member, Map<String, Object> attributes){
        this.member=member;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // member.getMemberRole().name()의 값을 권한으로 추가
        authorities.add(new SimpleGrantedAuthority(member.getMemberRole().name()));

        return authorities;
    }


    @Override
    public String getPassword() {

        return member.getMemberPwd();
    }

    public Long getId(){ //member의 id값 가져오기

        return member.getMemberId();
    }

    @Override
    public String getUsername() {

        return member.getMemberEmail();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }

    // OAuth2User //
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return member.getMemberNickName();
    }
}
