package org.sopt.controller;

import jakarta.validation.Valid;
import org.sopt.common.ApiResponse;
import org.sopt.dto.LoginRequest;
import org.sopt.dto.LoginResponse;
import org.sopt.dto.SignupRequest;
import org.sopt.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String REFRESH_COOKIE = "refresh_token";
    private static final String REFRESH_REQUEST_HEADER = "X-Refresh-Request";

    private final AuthService authService;
    private final long refreshTokenExpiration;
    private final boolean refreshCookieSecure;

    public AuthController(
            AuthService authService,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
            @Value("${jwt.refresh-cookie-secure}") boolean refreshCookieSecure
    ) {
        this.authService = authService;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.refreshCookieSecure = refreshCookieSecure;
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

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthService.IssuedTokens tokens = authService.login(request.email(), request.password());

        ResponseCookie refreshCookie = refreshCookie(
                tokens.refreshToken(), Duration.ofMillis(refreshTokenExpiration)
        );

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.success(
                        "LOGIN_SUCCESS", "로그인에 성공했습니다.", new LoginResponse(tokens.accessToken())
                ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @CookieValue(name = REFRESH_COOKIE, required = false) String refreshToken,
            @RequestHeader(name = REFRESH_REQUEST_HEADER, defaultValue = "") String refreshRequest
    ) {
        checkRefreshRequest(refreshRequest);
        String accessToken = authService.reissueAccessToken(refreshToken);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(ApiResponse.success(
                        "TOKEN_REFRESHED", "액세스 토큰을 재발급했습니다.", new LoginResponse(accessToken)
                ));
    }

    private void checkRefreshRequest(String refreshRequest) {
        if (!"1".equals(refreshRequest)) {
            throw new AccessDeniedException("허용되지 않은 인증 요청입니다.");
        }
    }

    private ResponseCookie refreshCookie(String value, Duration maxAge) {
        return ResponseCookie.from(REFRESH_COOKIE, value)
                .httpOnly(true)
                .secure(refreshCookieSecure)
                .sameSite("Strict")
                .path("/auth")
                .maxAge(maxAge)
                .build();
    }
}