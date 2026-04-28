package com.bugucloud.api.web.controller.auth;

import com.bugucloud.api.web.controller.auth.req.LoginReq;
import com.bugucloud.api.web.controller.auth.req.RegisterReq;
import com.bugucloud.api.web.controller.auth.vo.LoginVO;
import com.bugucloud.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
public class AuthController {

    @Operation(summary = "邮箱注册")
    @PostMapping("/register")
    public Result<LoginVO> register(@Valid @RequestBody RegisterReq request) {
        return Result.ok();
    }

    @Operation(summary = "邮箱登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginReq request) {
        return Result.ok();
    }
}
