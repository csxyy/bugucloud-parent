package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 功能描述: 文章评论表
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:12
 */
@TableName("t_comment")
@Data
@Schema(description = "文章评论表")
public class Comment extends BaseEntity {

    @Schema(description = "所属文章ID")
    @TableField(value = "article_id")
    private Long articleId;

    @Schema(description = "评论用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "冗余：评论时的用户名")
    @TableField(value = "username")
    private String username;

    @Schema(description = "冗余：评论时的头像")
    @TableField(value = "avatar")
    private String avatar;

    @Schema(description = "父评论ID 0=一级评论")
    @TableField(value = "parent_id")
    private Long parentId;

    @Schema(description = "被回复用户ID")
    @TableField(value = "reply_user_id")
    private Long replyUserId;

    @Schema(description = "冗余：被回复用户名")
    @TableField(value = "reply_username")
    private String replyUsername;

    @Schema(description = "评论内容")
    @TableField(value = "content")
    private String content;

    @Schema(description = "评论点赞数")
    @TableField(value = "likes")
    private Integer likes;

    @Schema(description = "是否删除 0=正常 1=已删除")
    @TableField(value = "is_deleted")
    private Integer isDeleted;
}
