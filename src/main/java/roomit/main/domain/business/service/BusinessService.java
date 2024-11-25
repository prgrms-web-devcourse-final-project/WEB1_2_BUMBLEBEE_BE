package roomit.main.domain.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.business.repository.BusinessRepository;
import roomit.main.domain.business.dto.request.BusinessRegisterRequest;
import roomit.main.domain.business.dto.request.BusinessUpdateRequest;
import roomit.main.domain.business.dto.response.BusinessResponse;
import roomit.main.global.error.ErrorCode;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    //사업자 등록
    @Transactional
    public void signUpBusiness(BusinessRegisterRequest registerRequest) {
        try {
            businessRepository.save(registerRequest.toEntity(passwordEncoder));
        }catch (Exception e) {
            throw ErrorCode.BUSINESS_NOT_REGISTER.commonException();
        }
    }

    //사업자 정보 조회
    @Transactional(readOnly = true)
    public BusinessResponse readBusinessInfo(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(ErrorCode.BUSINESS_NOT_FOUND::commonException);

        return new BusinessResponse(business);
    }


    //사업자 정보 수정
    @Transactional
    public void updateBusinessInfo(Long businessId, BusinessUpdateRequest updateRequest) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(ErrorCode.BUSINESS_NOT_FOUND::commonException);

        try {
            business.updateBusiness(updateRequest);

            businessRepository.save(business);
        }catch (Exception e){
            throw ErrorCode.BUSINESS_NOT_MODIFY.commonException();
        }
    }


    //사업자 탈퇴
    @Transactional
    public void deleteBusiness(Long businessId) {
        businessRepository.findById(businessId)
                .orElseThrow(ErrorCode.BUSINESS_NOT_FOUND::commonException);

        try {
            businessRepository.deleteById(businessId);
        }catch (Exception e){
            throw ErrorCode.BUSINESS_NOT_DELETE.commonException();
        }
    }
}
