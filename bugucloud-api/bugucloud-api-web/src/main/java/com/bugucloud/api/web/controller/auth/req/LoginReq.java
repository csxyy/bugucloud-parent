package com.bugucloud.api.web.controller.auth.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 功能描述: 登录请求
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 9:35
 */
@Data
@Schema(description = "登录请求")
public class LoginReq {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "登录邮箱")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;

    @Schema(description = "是否记住我")
    private Boolean rememberMe = false;
}
