package com.bugucloud.service.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 功能描述: 修改密码请求
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 9:52
 */

@Data
@Schema(description = "修改密码请求")
public class ChangePasswordReq {

    @NotBlank(message = "当前密码不能为空")
    @Schema(description = "当前密码")
    private String currentPassword;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码")
    private String newPassword;
}
