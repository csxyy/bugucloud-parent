package com.bugucloud.api.web.controller.auth.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 功能描述: 注册请求
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 9:36
 */

@Data
@Schema(description = "注册请求")
public class RegisterReq {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "注册邮箱")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度6-50位")
    @Schema(description = "密码")
    private String password;

    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称长度不能超过50位")
    @Schema(description = "昵称")
    private String nickname;
}
