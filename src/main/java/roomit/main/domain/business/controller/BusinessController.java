package roomit.main.domain.business.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.business.dto.request.BusinessRegisterRequest;
import roomit.main.domain.business.dto.request.BusinessUpdateRequest;
import roomit.main.domain.business.dto.response.BusinessResponse;
import roomit.main.domain.business.service.BusinessService;


@RestController
@RequestMapping("/api/v1/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    //사업자 회원 가입
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public void signUp(@RequestBody @Valid BusinessRegisterRequest request) {
        businessService.signUpBusiness(request);
    }

    //사업자 정보 수정
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping
    public void businessModify(@RequestBody @Valid BusinessUpdateRequest businessUpdateRequest,
                               @AuthenticationPrincipal CustomBusinessDetails customBusinessDetails){
        businessService.updateBusinessInfo(customBusinessDetails.getId(), businessUpdateRequest);
    }


    //사업자 정보 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public BusinessResponse businessRead(@AuthenticationPrincipal CustomBusinessDetails customBusinessDetails){
        return businessService.readBusinessInfo(customBusinessDetails.getId());
    }


    //사업자 탈퇴
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void businessDelete(@AuthenticationPrincipal CustomBusinessDetails customBusinessDetails){
        businessService.deleteBusiness(customBusinessDetails.getId());
    }

}
