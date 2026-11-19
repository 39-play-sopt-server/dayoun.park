package org.sopt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private static final String TOKEN_TYPE_CLAIM = "tokenType";
    private static final String ACCESS = "access";
    private static final String REFRESH = "refresh";

    private final SecretKey secretKey;
    private final JwtParser parser;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.parser = Jwts.parser().verifyWith(secretKey).build();
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String createAccessToken(Long memberId) {
        String tokenId = UUID.randomUUID().toString();
        return createToken(memberId, ACCESS, tokenId, accessTokenExpiration);
    }

    public String createRefreshToken(Long memberId, String refreshId) {
        return createToken(memberId, REFRESH, refreshId, refreshTokenExpiration);
    }

    public Long readAccessMemberId(String token) {
        Claims claims = parseSignedToken(token, ACCESS);
        return readMemberId(claims);
    }

    public RefreshIdentity readRefreshIdentity(String token) {
        Claims claims = parseSignedToken(token, REFRESH);
        String tokenId = claims.getId();

        if (tokenId == null || tokenId.isBlank()) {
            throw new JwtException("Refresh Token의 ID가 없습니다.");
        }

        return new RefreshIdentity(tokenId, readMemberId(claims));
    }

    private String createToken(Long memberId, String tokenType, String tokenId, long expirationMillis) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(memberId.toString())
                .id(tokenId)
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMillis)))
                .signWith(secretKey)
                .compact();
    }

    private Claims parseSignedToken(String token, String expectedType) {
        Claims claims = parser.parseSignedClaims(token).getPayload();

        if (claims.getExpiration() == null
                || !expectedType.equals(claims.get(TOKEN_TYPE_CLAIM, String.class))) {
            throw new JwtException("토큰 만료 정보 또는 용도가 올바르지 않습니다.");
        }

        return claims;
    }

    private Long readMemberId(Claims claims) {
        try {
            return Long.valueOf(claims.getSubject());
        } catch (NumberFormatException e) {
            throw new JwtException("토큰의 회원 ID가 올바르지 않습니다.", e);
        }
    }

    public record RefreshIdentity(String id, Long memberId) {}
}