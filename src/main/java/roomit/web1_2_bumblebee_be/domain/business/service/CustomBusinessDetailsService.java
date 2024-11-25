package roomit.web1_2_bumblebee_be.domain.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.business.entity.Business;
import roomit.web1_2_bumblebee_be.domain.business.repository.BusinessRepository;
import roomit.web1_2_bumblebee_be.domain.business.dto.CustomBusinessDetails;
import roomit.web1_2_bumblebee_be.global.error.ErrorCode;

@Service
@RequiredArgsConstructor
public class CustomBusinessDetailsService implements UserDetailsService {

    private final BusinessRepository businessRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Business business = businessRepository.findByBusinessEmail(email).orElseThrow(ErrorCode.BUSINESS_NOT_FOUND::commonException);

        if (business != null){

            return new CustomBusinessDetails(business);
        }
        return null;
    }
}
