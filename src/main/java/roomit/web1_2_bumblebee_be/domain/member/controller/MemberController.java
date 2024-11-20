package roomit.web1_2_bumblebee_be.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import roomit.web1_2_bumblebee_be.domain.member.request.MemberRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.member.request.MemberUpdateRequest;
import roomit.web1_2_bumblebee_be.domain.member.response.MemberResponse;
import roomit.web1_2_bumblebee_be.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;


    // 회원 등록
    @PostMapping("/signup")
    public void signup(@RequestBody @Valid MemberRegisterRequest request) {

        memberService.signupMember(request);
    }

    // 내 정보 조회
    @GetMapping("/{memberId}")
    public MemberResponse read(@PathVariable Long memberId){
        return memberService.read(memberId);
    }


    // 내 정보 수정
    @PutMapping("/{memberId}")
    public MemberResponse update(@PathVariable Long memberId, @RequestBody @Valid MemberUpdateRequest request) {
       return memberService.update(memberId, request);
    }

    // 내 정보 삭제
    @DeleteMapping("/{memberId}")
    public void delete(@PathVariable Long memberId) {
        memberService.delete(memberId);
    }
}
