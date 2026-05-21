package com.bugucloud.service.auth;

import com.bugucloud.core.vo.LoginVO;
import com.bugucloud.core.vo.TokenRefreshVO;
import com.bugucloud.service.req.LoginReq;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/21 - 22:54
 */
public interface AuthService {
    /**
     * 用户登录
     * @param req 登录请求
     * @return 登录响应
     */
    LoginVO login(LoginReq req);

    /**
     * 刷新Token
     * @param refreshToken 刷新令牌
     * @return 新的Token对
     */
    TokenRefreshVO refreshToken(String refreshToken);

    /**
     * 用户登出
     * @param refreshToken 刷新令牌
     */
    void logout(String refreshToken);
}
