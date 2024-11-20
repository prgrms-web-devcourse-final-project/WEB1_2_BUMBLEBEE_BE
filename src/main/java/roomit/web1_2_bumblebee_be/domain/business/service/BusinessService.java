package roomit.web1_2_bumblebee_be.domain.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomit.web1_2_bumblebee_be.domain.business.entity.Business;
import roomit.web1_2_bumblebee_be.domain.business.exception.BusinessNotDelete;
import roomit.web1_2_bumblebee_be.domain.business.exception.BusinessNotFound;
import roomit.web1_2_bumblebee_be.domain.business.exception.BusinessNotModify;
import roomit.web1_2_bumblebee_be.domain.business.exception.BusinessNotRegister;
import roomit.web1_2_bumblebee_be.domain.business.repository.BusinessRepository;
import roomit.web1_2_bumblebee_be.domain.business.request.BusinessRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.business.request.BusinessUpdateRequest;
import roomit.web1_2_bumblebee_be.domain.business.response.BusinessResponse;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;

    //사업자 등록
    @Transactional
    public void signUpBusiness(BusinessRegisterRequest registerRequest) {
        try {
            Business business = Business.builder()
                    .businessName(registerRequest.getBusinessName())
                    .businessEmail(registerRequest.getBusinessEmail())
                    //비밀 번호 암호화 처리 필요
                    .businessPwd(registerRequest.getBusinessPwd())
                    .businessNum(registerRequest.getBusinessNum())
                    .build();

            // 예외 처리 필요
            businessRepository.save(business);
        }catch (Exception e) {
            throw new BusinessNotRegister();
        }
    }

    //사업자 정보 조회
    @Transactional(readOnly = true)
    public BusinessResponse readBusinessInfo(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(BusinessNotFound::new);

        return new BusinessResponse(business);
    }


    //사업자 정보 수정
    @Transactional
    public void updateBusinessInfo(Long businessId, BusinessUpdateRequest updateRequest) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(BusinessNotFound::new);

        try {
            business.changeBusinessEmail(updateRequest.getBusinessEmail());
            business.changeBusinessName(updateRequest.getBusinessName());
            business.changeBusinessNum(updateRequest.getBusinessNum());

            businessRepository.save(business);
        }catch (Exception e){
            throw new BusinessNotModify();
        }
    }


    //사업자 탈퇴
    @Transactional
    public void deleteBusiness(Long businessId) {
        businessRepository.findById(businessId)
                .orElseThrow(BusinessNotFound::new);

        try {
            businessRepository.deleteById(businessId);
        }catch (Exception e){
            throw new BusinessNotDelete();
        }
    }
}
