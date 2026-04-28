package com.bugucloud.api.web.controller.auth.vo;

import lombok.Data;

/**
 * 功能描述: 登录/注册返回VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 9:37
 */

@Data
public class LoginVO {

    /** 用户ID */
    private Long userId;

    /** 用户昵称 */
    private String nickname;

    /** 用户头像 */
    private String avatar;

    /** 身份标识 */
    private Integer role;

    /** Token */
    private String token;
}
