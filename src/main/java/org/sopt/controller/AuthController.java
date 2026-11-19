package org.sopt.controller;

import jakarta.validation.Valid;
import org.sopt.common.ApiResponse;
import org.sopt.dto.LoginRequest;
import org.sopt.dto.LoginResponse;
import org.sopt.dto.SignupRequest;
import org.sopt.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        Long memberId = authService.signup(
                request.name(),
                request.email(),
                request.password()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                "MEMBER_CREATED",
                "회원이 생성되었습니다.",
                memberId
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        String accessToken = authService.login(request.email(), request.password());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "LOGIN_SUCCESS",
                        "로그인에 성공했습니다.",
                        new LoginResponse(accessToken)
                )
        );
    }
}
