package roomit.web1_2_bumblebee_be.domain.business.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomit.web1_2_bumblebee_be.domain.business.entity.Business;
import roomit.web1_2_bumblebee_be.domain.business.exception.BusinessNotFound;
import roomit.web1_2_bumblebee_be.domain.business.repository.BusinessRepository;
import roomit.web1_2_bumblebee_be.domain.business.request.BusinessRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.business.request.BusinessUpdateRequest;
import roomit.web1_2_bumblebee_be.domain.business.response.BusinessResponse;
import roomit.web1_2_bumblebee_be.domain.business.service.BusinessService;


@RestController
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    //사업자 회원 가입
    @PostMapping("/api/v1/business/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid BusinessRegisterRequest request) {
        businessService.signUpBusiness(request);

        return ResponseEntity.status(200).body("사업자 회원 등록이 완료되었습니다.");
    }


    //사업자 정보 수정
    @PutMapping("api/v1/business/update")
    public ResponseEntity<String> businessModify(@RequestBody @Valid BusinessUpdateRequest businessUpdateRequest
                                                 /*,@AuthenticationPrincipal CustomMemberDetails customMember*/){

        businessService.updateBusinessInfo(1L /* customMember.userId */, businessUpdateRequest);

        return ResponseEntity.status(200).body("사업자 정보 수정이 성공적으로 완료되었습니다.");
    }


    //사업자 정보 조회
    @GetMapping("api/v1/business")
    public ResponseEntity<BusinessResponse> businessRead(/*@AuthenticationPrincipal CustomUserDetails customUser*/){

        return ResponseEntity.status(200).body(businessService.readBusinessInfo(1L));
    }


    //사업자 탈퇴
    @DeleteMapping("api/v1/business")
    public ResponseEntity<String> businessDelete(/*@AuthenticationPrincipal CustomMemberDetails customMember*/){

        businessService.deleteBusiness(1L);

        return ResponseEntity.status(200).body("사업자가 성공적으로 탈퇴되었습니다.");
    }

}
