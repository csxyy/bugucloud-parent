package com.bugucloud.core.vo;

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

    /** 用户昵称 */
    private String nickname;

    /** 用户邮箱 */
    private String email;

    /** 用户会员等级 身份标识 0=普通用户 1=会员 2=超级会员 3=管理员 */
    private Integer role;
}
