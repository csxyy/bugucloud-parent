package com.bugucloud.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/13 - 22:25
 */

@Component
public class JwtUtils {

    // 新版强制要求使用 Key 对象，不能直接传字符串。密钥长度需与算法匹配（HS256至少256位）
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "YourSuperSecretKeyForJWTWhichMustBeLongEnoughForHS256!".getBytes()
    );

    private static final long ACCESS_EXPIRATION_MS = 3 * 60 * 1000; // 15 分钟
    private static final long REFRESH_EXPIRATION_MS = 30L * 24 * 60 * 60 * 1000; // 30 天

    // ==================== 生成 Access Token ====================
    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_EXPIRATION_MS);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    // ==================== 生成 Refresh Token ====================
    // 可以带一个唯一ID用于检测重用
    public String generateRefreshToken(String username) {
        String tokenId = UUID.randomUUID().toString();  // 每次生成唯一ID

        return Jwts.builder()
                .subject(username)
                .id(tokenId)    // 设置 jti，用于唯一标识
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_MS))
                .signWith(SECRET_KEY)
                .compact();
    }

    // 从 Refresh Token 中取出 jti，用于判断是否被重用
    public String getTokenId(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getId();
    }

    // ==================== 通用方法 ====================
    // 从 JWT 中解析出用户名
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    // 校验 JWT 是否有效
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
