package roomit.main.domain.business.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.business.dto.request.BusinessRegisterRequest;
import roomit.main.domain.business.dto.request.BusinessUpdateRequest;
import roomit.main.domain.business.dto.response.BusinessResponse;
import roomit.main.domain.business.service.BusinessService;


@RestController
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    //사업자 회원 가입
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/business/signup")
    public void signup(@RequestBody @Valid BusinessRegisterRequest request) {
        businessService.signUpBusiness(request);
    }

    //사업자 정보 수정
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("api/v1/business/update")
    public void businessModify(@RequestBody @Valid BusinessUpdateRequest businessUpdateRequest
                               ,@AuthenticationPrincipal CustomBusinessDetails customBusinessDetails){
        businessService.updateBusinessInfo(customBusinessDetails.getId(), businessUpdateRequest);
    }


    //사업자 정보 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("api/v1/business")
    public BusinessResponse businessRead(@AuthenticationPrincipal CustomBusinessDetails customBusinessDetails){
        return businessService.readBusinessInfo(customBusinessDetails.getId());
    }


    //사업자 탈퇴
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("api/v1/business")
    public void businessDelete(@AuthenticationPrincipal CustomBusinessDetails customBusinessDetails){
        businessService.deleteBusiness(customBusinessDetails.getId());
    }

}
