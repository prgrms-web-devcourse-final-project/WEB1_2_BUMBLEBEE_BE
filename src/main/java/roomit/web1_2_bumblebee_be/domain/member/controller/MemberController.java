package roomit.web1_2_bumblebee_be.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomit.web1_2_bumblebee_be.domain.member.dto.request.MemberRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.member.dto.request.MemberUpdateRequest;
import roomit.web1_2_bumblebee_be.domain.member.dto.response.MemberResponse;
import roomit.web1_2_bumblebee_be.domain.member.service.MemberService;

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
    @GetMapping("/api/v1/member/{memberId}")
    public MemberResponse read(@PathVariable Long memberId){
        return memberService.read(memberId);
    }


    // 내 정보 수정
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/api/v1/member/{memberId}")
    public MemberResponse update(@PathVariable Long memberId, @RequestBody @Valid MemberUpdateRequest request) {
        return memberService.update(memberId, request);
    }

    // 내 정보 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/member/{memberId}")
    public void delete(@PathVariable Long memberId) {
        memberService.delete(memberId);
    }
}
