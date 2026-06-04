package com.bugucloud.service.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "旧密码不能为空")
    @Schema(description = "旧密码")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "新密码长度6-20位")
    @Schema(description = "新密码")
    private String newPassword;

    @NotBlank(message = "确认新密码不能为空")
    @Schema(description = "确认新密码")
    private String confirmNewPassword;
}
