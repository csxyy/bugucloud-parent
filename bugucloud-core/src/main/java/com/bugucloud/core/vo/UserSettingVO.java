package com.bugucloud.core.vo;

import lombok.Data;

/**
 * 功能描述: 用户信息管理VO（返回）
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 9:49
 */

@Data
public class UserSettingVO {

    /** 用户ID */
    private Long userId;

    /** 用户头像 */
    private String avatar;

    /** 用户名 */
    //private String username;

    /** 昵称 */
    private String nickname;

    /** 邮箱 */
    private String email;

    /** 标签 */
    //private List<TagVO> tags;

    /** 个人简介 */
    private String personalIntro;
}
