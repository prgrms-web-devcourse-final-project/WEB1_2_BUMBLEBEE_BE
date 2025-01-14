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
import roomit.main.global.exception.CommonException;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    //사업자 등록
    @Transactional
    public void signUpBusiness(BusinessRegisterRequest registerRequest) {

        //중복 이메일 검증
        if(businessRepository.existsByBusinessEmail(registerRequest.businessEmail())){
            throw ErrorCode.BUSINESS_EMAIL_DUPLICATION.commonException();
        }

        //중복 사업자 이름 검증
        if(businessRepository.existsByBusinessName(registerRequest.businessName())){
            throw ErrorCode.BUSINESS_NICKNAME_DUPLICATION.commonException();
        }

        //중복 사업자 번호 검증
        if(businessRepository.existsByBusinessNum(registerRequest.businessNum())){
            throw ErrorCode.BUSINESS_NUMBER_DUPLICATION.commonException();
        }

        //사업자 등록
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
        try {
            businessRepository.updateBusiness(updateRequest, businessId);
        }catch (Exception e){
            // 기존 오류 코드가 담긴 예외를 그대로 던짐
            if (e instanceof CommonException) {
                throw (CommonException) e;
            } else {
                throw ErrorCode.BUSINESS_NOT_MODIFY.commonException();
            }
        }
    }

    //사업자 탈퇴
    @Transactional
    public void deleteBusiness(Long businessId) {
        Business existBusiness = businessRepository.findById(businessId)
                .orElseThrow(ErrorCode.BUSINESS_NOT_FOUND::commonException);

        try {
            businessRepository.deleteById(businessId);
        }catch (Exception e){
            throw ErrorCode.BUSINESS_NOT_DELETE.commonException();
        }
    }
}
