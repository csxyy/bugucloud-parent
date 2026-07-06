package com.bugucloud.service.auth;

import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.common.security.jwt.JwtUtils;
import com.bugucloud.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    private final RedisUtil redisUtil;

    private static final String USED_TOKEN_PREFIX = "used_jti:";
    private static final String USER_TOKENS_PREFIX = "user_tokens:";

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
        String usedTokenKey = USED_TOKEN_PREFIX + tokenId;
        if (redisUtil.hasKey(usedTokenKey)) {
            Long userId = jwtUtils.getUserIdFromToken(oldRefreshToken);
            revokeAllTokensForUser(userId);
            throw new BusinessException("检测到 Token 泄露，请重新登录");
        }

        // 3. 标记旧 Token 为已使用，过期时间与 Refresh Token 保持一致
        redisUtil.set(usedTokenKey, "1", JwtUtils.REFRESH_EXPIRATION_MS, TimeUnit.MILLISECONDS);

        // 4. 生成新的 Token 对
        Long userId = jwtUtils.getUserIdFromToken(oldRefreshToken);
        String newAccessToken = jwtUtils.generateAccessToken(userId);
        String newRefreshToken = jwtUtils.generateRefreshToken(userId);

        // 5. 将新 Token 记录到用户令牌池
        String newTokenId = jwtUtils.getTokenId(newRefreshToken);
        String userKey = USER_TOKENS_PREFIX + userId;
        redisUtil.sAdd(userKey, newTokenId);
        // 设置用户令牌集合的过期时间，与 Refresh Token 保持一致
        redisUtil.expire(userKey, JwtUtils.REFRESH_EXPIRATION_MS, TimeUnit.MILLISECONDS);

        return new TokenPair(newAccessToken, newRefreshToken);
    }

    /**
     * 登录时调用：生成初始 Token 对并登记
     */
    public TokenPair createInitialTokens(Long userId) {
        String accessToken = jwtUtils.generateAccessToken(userId);
        String refreshToken = jwtUtils.generateRefreshToken(userId);

        String tokenId = jwtUtils.getTokenId(refreshToken);
        String userKey = USER_TOKENS_PREFIX + userId;
        redisUtil.sAdd(userKey, tokenId);
        // 设置用户令牌集合的过期时间，与 Refresh Token 保持一致
        redisUtil.expire(userKey, JwtUtils.REFRESH_EXPIRATION_MS, TimeUnit.MILLISECONDS);

        return new TokenPair(accessToken, refreshToken);
    }

    /**
     * 撤销某用户的所有 Refresh Token（Token 泄露时调用）
     */
    public void revokeAllTokensForUser(Long userId) {
        String userKey = USER_TOKENS_PREFIX + userId;
        Set<Object> tokenIds = redisUtil.sMembers(userKey);
        if (tokenIds != null && !tokenIds.isEmpty()) {
            // 将所有 Token 标记为已使用，过期时间与 Refresh Token 保持一致
            for (Object tokenId : tokenIds) {
                String usedTokenKey = USED_TOKEN_PREFIX + tokenId.toString();
                redisUtil.set(usedTokenKey, "1", JwtUtils.REFRESH_EXPIRATION_MS, TimeUnit.MILLISECONDS);
            }
            // 删除用户令牌集合
            redisUtil.delete(userKey);
        }
    }

    /**
     * 用户登出时调用
     */
    public void logout(String refreshToken) {
        String tokenId = jwtUtils.getTokenId(refreshToken);
        String usedTokenKey = USED_TOKEN_PREFIX + tokenId;
        redisUtil.set(usedTokenKey, "1", JwtUtils.REFRESH_EXPIRATION_MS, TimeUnit.MILLISECONDS);
    }

    public record TokenPair(String accessToken, String refreshToken) {}
}