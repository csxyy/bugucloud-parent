package com.bugucloud.service.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 功能描述: 用户信息更新请求
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 9:50
 */
@Data
@Schema(description = "用户信息更新请求")
public class UserUpdateReq {

    @Schema(description = "用户头像URL")
    private String avatar;

//    @Size(max = 50, message = "用户名长度不能超过50位")
//    @Schema(description = "用户名")
//    private String username;

    @Size(max = 50, message = "昵称长度不能超过50位")
    @Schema(description = "昵称")
    private String nickname;

//    @Schema(description = "标签ID列表")
//    private List<Long> tagIds;

    @Size(max = 500, message = "个人简介长度不能超过500字")
    @Schema(description = "个人简介")
    private String personalIntro;
}
