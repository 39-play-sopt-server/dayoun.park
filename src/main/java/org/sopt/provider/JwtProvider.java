package org.sopt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String createAccessToken(Long memberId) {
        return createToken(memberId, "access", UUID.randomUUID().toString(), accessTokenExpiration);
    }

    public String createRefreshToken(Long memberId, String refreshId) {
        return createToken(memberId, "refresh", refreshId, refreshTokenExpiration);
    }

    private String createToken(Long memberId, String tokenType, String tokenId, long expirationMillis) {
        Date now = new Date();

        return Jwts.builder()
                .subject(memberId.toString())
                .id(tokenId)
                .claim("tokenType", tokenType)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMillis))
                .signWith(secretKey)
                .compact();
    }

    public Long getMemberId(String token) {
        return Long.valueOf(parseToken(token, "access").getSubject());
    }

    public Claims parseRefreshToken(String token) {
        return parseToken(token, "refresh");
    }

    private Claims parseToken(String token, String expectedType) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        if (claims.getExpiration() == null
                || !expectedType.equals(claims.get("tokenType", String.class))) {
            throw new JwtException("토큰 만료 정보 또는 용도가 올바르지 않습니다.");
        }

        return claims;
    }
}