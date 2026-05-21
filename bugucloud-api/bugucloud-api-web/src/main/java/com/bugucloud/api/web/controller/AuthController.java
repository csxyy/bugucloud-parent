package com.bugucloud.api.web.controller;

import com.bugucloud.common.result.Result;
import com.bugucloud.core.vo.LoginVO;
import com.bugucloud.core.vo.TokenRefreshVO;
import com.bugucloud.service.auth.AuthService;
import com.bugucloud.service.req.LoginReq;
import com.bugucloud.service.req.RefreshReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述: 登录注册
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/27 - 23:35
 */
@Tag(name = "登录注册")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginReq req) {
        LoginVO loginVO = authService.login(req);
        return Result.ok(loginVO);
    }

    /**
     * 刷新 Token
     */
    @Operation(summary = "刷新Token")
    @PostMapping("/refresh")
    public Result<TokenRefreshVO> refresh(@RequestBody RefreshReq req) {
        TokenRefreshVO tokenRefreshVO = authService.refreshToken(req.getRefreshToken());
        return Result.ok(tokenRefreshVO);
    }

    /**
     * 用户登出
     */
    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<String> logout(@RequestBody RefreshReq req) {
        authService.logout(req.getRefreshToken());
        return Result.ok("登出成功", null);
    }


}
