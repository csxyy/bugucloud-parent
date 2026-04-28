package com.bugucloud.api.web.controller.user.vo;

import lombok.Data;

/**
 * 功能描述: 用户基本信息VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 9:48
 */

@Data
public class UserInfoVO {

    /** 用户ID */
    private Long userId;

    /** 用户头像URL */
    private String avatar;

    /** 用户邮箱 */
    private String email;

    /** 用户会员等级 */
    private Integer role;
}
