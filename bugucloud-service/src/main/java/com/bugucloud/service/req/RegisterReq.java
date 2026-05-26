package com.bugucloud.service.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/25 - 22:51
 */

@Data
@Schema(description = "注册请求")
public class RegisterReq {

    @NotBlank(message = "邮箱地址不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱地址")
    private String email;

    @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码")
    private String code;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20位")
    @Schema(description = "设置密码")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @Schema(description = "确认密码")
    private String confirmPassword;
}
