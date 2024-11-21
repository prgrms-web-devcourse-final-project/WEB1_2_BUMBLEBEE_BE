package roomit.web1_2_bumblebee_be.domain.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.business.entity.Business;
import roomit.web1_2_bumblebee_be.domain.business.exception.BusinessNotFound;
import roomit.web1_2_bumblebee_be.domain.business.repository.BusinessRepository;
import roomit.web1_2_bumblebee_be.domain.business.response.CustomBusinessDetails;
import roomit.web1_2_bumblebee_be.domain.member.dto.CustomMemberDetails;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CustomBusinessDetailsService implements UserDetailsService {

    private final BusinessRepository businessRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Business business = businessRepository.findByBusinessEmail(email).orElseThrow(BusinessNotFound::new);

        if (business != null){

            return new CustomBusinessDetails(business);
        }
        return null;
    }
}
