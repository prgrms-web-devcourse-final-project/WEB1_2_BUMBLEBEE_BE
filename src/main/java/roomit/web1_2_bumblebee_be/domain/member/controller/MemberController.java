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
    @PostMapping("/api/v1/member/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid MemberRegisterRequest request) {
        memberService.signupMember(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 내 정보 조회
    @GetMapping("/api/v1/member/{memberId}")
    public ResponseEntity<MemberResponse> read(@PathVariable Long memberId){
        return ResponseEntity.status(HttpStatus.OK).body(memberService.read(memberId));
    }


    // 내 정보 수정
    @PutMapping("/api/v1/member/{memberId}")
    public ResponseEntity<MemberResponse> update(@PathVariable Long memberId, @RequestBody @Valid MemberUpdateRequest request) {
       return ResponseEntity.status(HttpStatus.OK).body(memberService.update(memberId, request));
    }

    // 내 정보 삭제
    @DeleteMapping("/api/v1/member/{memberId}")
    public ResponseEntity<?> delete(@PathVariable Long memberId) {
        memberService.delete(memberId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
