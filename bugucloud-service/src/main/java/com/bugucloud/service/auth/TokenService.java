package com.bugucloud.service.auth;

import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.common.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/20 - 9:05
 */


@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtils jwtUtils;

    // 实际项目用 Redis：Key = "used_jti:" + tokenId, Value = "1", TTL = 剩余有效期
    private final Set<String> usedRefreshTokens = ConcurrentHashMap.newKeySet();

    // Key = "user_tokens:" + userId, Value = Set<tokenId>
    private final ConcurrentHashMap<String, Set<String>> userTokenMap = new ConcurrentHashMap<>();

    /**
     * 刷新 Access Token，同时轮换 Refresh Token（以旧换新）
     */
    public TokenPair refresh(String oldRefreshToken) {
        // 1. 验证旧 Refresh Token 是否有效
        if (!jwtUtils.validateToken(oldRefreshToken)) {
            throw new BusinessException("Refresh Token 已过期，请重新登录");
        }

        // 2. 【重用检测】检查这个 Token 是否已经被用过
        String tokenId = jwtUtils.getTokenId(oldRefreshToken);
        if (usedRefreshTokens.contains(tokenId)) {
            Long userId = jwtUtils.getUserIdFromToken(oldRefreshToken);
            revokeAllTokensForUser(userId);
            throw new BusinessException("检测到 Token 泄露，请重新登录");
        }

        // 3. 标记旧 Token 为已使用
        usedRefreshTokens.add(tokenId);

        // 4. 生成新的 Token 对
        Long userId = jwtUtils.getUserIdFromToken(oldRefreshToken);
        String newAccessToken = jwtUtils.generateAccessToken(userId);
        String newRefreshToken = jwtUtils.generateRefreshToken(userId);

        // 5. 将新 Token 记录到用户令牌池
        String newTokenId = jwtUtils.getTokenId(newRefreshToken);
        String userKey = "user_tokens:" + userId;
        userTokenMap
                .computeIfAbsent(userKey, k -> ConcurrentHashMap.newKeySet())
                .add(newTokenId);

        return new TokenPair(newAccessToken, newRefreshToken);
    }

    /**
     * 登录时调用：生成初始 Token 对并登记
     */
    public TokenPair createInitialTokens(Long userId) {
        String accessToken = jwtUtils.generateAccessToken(userId);
        String refreshToken = jwtUtils.generateRefreshToken(userId);

        String tokenId = jwtUtils.getTokenId(refreshToken);
        String userKey = "user_tokens:" + userId;
        userTokenMap
                .computeIfAbsent(userKey, k -> ConcurrentHashMap.newKeySet())
                .add(tokenId);

        return new TokenPair(accessToken, refreshToken);
    }

    /**
     * 撤销某用户的所有 Refresh Token（Token 泄露时调用）
     */
    public void revokeAllTokensForUser(Long userId) {
        String userKey = "user_tokens:" + userId;
        Set<String> tokenIds = userTokenMap.remove(userKey);
        if (tokenIds != null) {
            usedRefreshTokens.addAll(tokenIds);
        }
    }

    /**
     * 用户登出时调用
     */
    public void logout(String refreshToken) {
        String tokenId = jwtUtils.getTokenId(refreshToken);
        usedRefreshTokens.add(tokenId);
    }

    public record TokenPair(String accessToken, String refreshToken) {}
}
