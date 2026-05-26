package com.bugucloud.service.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/25 - 22:52
 */

@Data
@Schema(description = "发送验证码请求")
public class SendCodeReq {

    @NotBlank(message = "邮箱地址不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱地址")
    private String email;
}
