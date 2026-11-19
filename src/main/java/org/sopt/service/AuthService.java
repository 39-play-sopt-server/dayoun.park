package org.sopt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.sopt.model.Member;
import org.sopt.model.RefreshToken;
import org.sopt.provider.JwtProvider;
import org.sopt.repository.MemberRepository;
import org.sopt.repository.RefreshTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public Long signup(String name, String email, String password) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(password);

        Member member = new Member(name, email, encodedPassword);
        return memberRepository.save(member).getId();
    }

    @Transactional
    public IssuedTokens login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."
                ));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."
            );
        }

        String refreshId = UUID.randomUUID().toString();
        String accessToken = jwtProvider.createAccessToken(member.getId());
        String refreshToken = jwtProvider.createRefreshToken(member.getId(), refreshId);

        refreshTokenRepository.save(new RefreshToken(refreshId, member));
        return new IssuedTokens(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public String reissueAccessToken(String refreshToken) {
        RefreshIdentity refresh = readRefreshIdentity(refreshToken);

        if (!refreshTokenRepository.existsByIdAndMember_Id(refresh.id(), refresh.memberId())) {
            throw invalidRefreshToken();
        }

        return jwtProvider.createAccessToken(refresh.memberId());
    }

    private RefreshIdentity readRefreshIdentity(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw invalidRefreshToken();
        }

        try {
            Claims claims = jwtProvider.parseRefreshToken(refreshToken);
            Long memberId = Long.valueOf(claims.getSubject());

            if (claims.getId() == null || claims.getId().isBlank()) {
                throw invalidRefreshToken();
            }

            return new RefreshIdentity(claims.getId(), memberId);
        } catch (JwtException | IllegalArgumentException e) {
            throw invalidRefreshToken();
        }
    }

    private ResponseStatusException invalidRefreshToken() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."
        );
    }

    private record RefreshIdentity(String id, Long memberId) {}

    public record IssuedTokens(String accessToken, String refreshToken) {}
}