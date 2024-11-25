package roomit.main.domain.business.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import roomit.main.domain.business.entity.Business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomBusinessDetails implements UserDetails, OAuth2User {

    private final Business business;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(business.getBusinessRole().name()));

        return authorities;
    }


    @Override
    public String getPassword() {

        return business.getBusinessPwd().getValue();
    }

    public Long getId(){ //business id값 가져오기

        return business.getBusinessId();
    }

    @Override
    public String getUsername() {

        return business.getBusinessEmail();
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
        return null;
    }

    @Override
    public String getName() {
        return business.getBusinessName();
    }
}
