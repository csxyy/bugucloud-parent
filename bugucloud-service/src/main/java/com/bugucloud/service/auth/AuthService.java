package com.bugucloud.service.auth;

import com.bugucloud.core.vo.LoginVO;
import com.bugucloud.core.vo.TokenRefreshVO;
import com.bugucloud.service.req.LoginReq;
import com.bugucloud.service.req.RegisterReq;
import com.bugucloud.service.req.ResetPasswordReq;

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

    /**
     * 发送验证码
     * @param email 邮箱地址
     */
    void sendVerificationCode(String email);

    /**
     * 用户注册
     * @param req 注册请求
     */
    void register(RegisterReq req);

    /**
     * 找回密码
     * @param req 找回密码请求
     */
    void resetPassword(ResetPasswordReq req);
}
