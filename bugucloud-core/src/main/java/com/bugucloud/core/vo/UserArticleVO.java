package com.bugucloud.core.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能描述: 用户主页文章查询VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/9 - 11:40
 */
@Data
public class UserArticleVO {

    /** 文章ID */
    private Long articleId;

    /** 文章封面URL */
    private String cover;

    /** 文章标题 */
    private String title;

    /** 文章摘要 */
    private String summary;

    /** 发布时间 */
    private LocalDateTime createTime;

    /** 阅读数 */
    private Long views;

    /** 点赞数 */
    private Integer likes;

    /** 评论数 */
    private Integer comments;

    /** 收藏数 */
    private Integer collects;
}
