package roomit.web1_2_bumblebee_be.domain.business.response;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import roomit.web1_2_bumblebee_be.domain.business.entity.Business;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomBusinessDetails implements UserDetails {

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

    public Long getId(){ //member의 id값 가져오기

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
}
