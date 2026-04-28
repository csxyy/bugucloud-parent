package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 功能描述: 文章信息表
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:12
 */
@TableName("t_article")
@Data
@Schema(description = "文章信息表")
public class Article extends BaseEntity {

    @Schema(description = "作者用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "文章标题")
    @TableField(value = "title")
    private String title;

    @Schema(description = "文章副标题")
    @TableField(value = "subtitle")
    private String subtitle;

    @Schema(description = "文章封面图URL")
    @TableField(value = "cover")
    private String cover;

    @Schema(description = "文章内容")
    @TableField(value = "content")
    private String content;

    @Schema(description = "阅读量")
    @TableField(value = "views")
    private Long views;

    @Schema(description = "点赞量")
    @TableField(value = "likes")
    private Integer likes;

    @Schema(description = "评论量")
    @TableField(value = "comments")
    private Integer comments;

    @Schema(description = "收藏量")
    @TableField(value = "collects")
    private Integer collects;

    @Schema(description = "分享数")
    @TableField(value = "shares")
    private Integer shares;

    @Schema(description = "是否发布 0=草稿 1=已发布 2=下架")
    @TableField(value = "is_published")
    private Integer isPublished;

    @Schema(description = "内容状态 0-待审核 1-审核通过 2-违规拉黑")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "排序号，越大越靠前")
    @TableField(value = "sort")
    private Integer sort;
}
