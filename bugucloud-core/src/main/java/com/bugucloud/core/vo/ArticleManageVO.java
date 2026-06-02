package com.bugucloud.core.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能描述: 文章管理查询
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 16:00
 */
@Data
public class ArticleManageVO {
    /** 文章ID */
    private Long articleId;

    /** 文章标题 */
    private String title;

    /** 文章摘要 */
    private String summary;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 审核状态 0-未提交 1-审核中 2-审核通过 3-审核驳回 */
    private Integer auditStatus;

    /** 发布状态 0-草稿 1-已发布 2-下架 */
    private Integer isPublished;

    /** 阅读数 */
    private Long views;

    /** 点赞数 */
    private Integer likes;

    /** 评论数 */
    private Integer comments;

    /** 收藏数 */
    private Integer collects;
}
