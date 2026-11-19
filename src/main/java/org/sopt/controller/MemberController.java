package org.sopt.controller;

import org.sopt.common.ApiResponse;
import org.sopt.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Long>> me(
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "MEMBER_ME_READ",
                        "내 회원 정보를 조회했습니다.",
                        memberId
                )
        );
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @AuthenticationPrincipal Long memberId
    ) {
        memberService.removeMember(memberId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "MEMBER_DELETED",
                        "회원이 삭제되었습니다.",
                        null
                ));
    }
}