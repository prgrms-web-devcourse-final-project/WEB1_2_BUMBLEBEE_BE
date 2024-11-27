package roomit.main.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.member.dto.request.MemberRegisterRequest;
import roomit.main.domain.member.dto.request.MemberUpdateRequest;
import roomit.main.domain.member.dto.response.MemberResponse;
import roomit.main.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 등록
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/member/signup")
    public void signup(@RequestBody @Valid MemberRegisterRequest request) {
        memberService.signupMember(request);
    }

    // 내 정보 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/member/")
    public MemberResponse read(@AuthenticationPrincipal CustomMemberDetails customMemberDetails){
        return memberService.read(customMemberDetails.getId());
    }


    // 내 정보 수정
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/api/v1/member/")
    public MemberResponse update(@AuthenticationPrincipal CustomMemberDetails customMemberDetails, @RequestBody @Valid MemberUpdateRequest request) {
        return memberService.update(customMemberDetails.getId(), request);
    }

    // 내 정보 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/member/")
    public void delete(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        memberService.delete(customMemberDetails.getId());
    }
}
