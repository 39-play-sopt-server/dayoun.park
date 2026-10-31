package org.sopt.controller;

import org.sopt.common.ApiResponse;
import org.sopt.model.Member;
import org.sopt.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Member>> createMember(
            @RequestParam("name") String name
    ) {
        Member member = memberService.createMember(name);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "MEMBER_CREATED",
                        "회원이 생성되었습니다.",
                        member
                ));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Void>> removeMember (
            @PathVariable("memberId") Long memberId
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