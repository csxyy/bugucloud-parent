package com.bugucloud.core.vo;

import lombok.Data;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/27 - 22:31
 */
@Data
public class AuthorBriefVO {
    /** 用户ID */
    private Long id;

    /** 用户昵称 */
    private String nickname;

    /** 用户头像URL */
    private String avatar;
}
