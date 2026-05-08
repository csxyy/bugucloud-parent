package com.bugucloud.core.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能描述: 管理后台-查询我的评论列表
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 22:22
 */
@Data
public class MyCommentVO {

    /** 用户ID */
    private Long userId;

    /** 用户头像 */
    private String avatar;

    /** 用户昵称 */
    private String nickname;

    /** 评论内容 */
    private String content;

    /** 发布时间 */
    private LocalDateTime createTime;

    /** 文章ID */
    private Long articleId;

    /** 文章封面URL */
    private String articleCover;
}
