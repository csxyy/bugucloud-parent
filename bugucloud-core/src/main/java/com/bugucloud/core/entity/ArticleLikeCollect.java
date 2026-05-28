package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 功能描述: 文章点赞收藏记录表
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:12
 */
@TableName("t_article_like_collect")
@Data
@Schema(description = "文章点赞收藏记录表")
public class ArticleLikeCollect extends BaseEntity {

    @Schema(description = "操作人ID(点赞/收藏者)")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "头像URL(冗余存储)")
    @TableField(value = "user_avatar")
    private String userAvatar;

    @Schema(description = "关联文章ID")
    @TableField(value = "article_id")
    private Long articleId;

    @Schema(description = "文章标题(冗余存储)")
    @TableField(value = "article_title")
    private String articleTitle;

    @Schema(description = "被操作人ID(文章作者)")
    @TableField(value = "target_user_id")
    private Long targetUserId;

    @Schema(description = "操作类型 1=点赞 2=收藏")
    @TableField(value = "msg_type")
    private Integer msgType;

    @Schema(description = "是否取消 0=有效 1=已取消")
    @TableField(value = "is_cancel")
    private Integer isCancel;
}
