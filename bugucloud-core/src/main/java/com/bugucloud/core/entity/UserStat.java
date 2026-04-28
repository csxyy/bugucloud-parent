package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 功能描述: 用户统计信息表
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:12
 */
@TableName("t_user_stat")
@Data
@Schema(description = "用户统计信息表")
public class UserStat extends BaseEntity {

    @Schema(description = "关联用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "总访问量")
    @TableField(value = "total_views")
    private Long totalViews;

    @Schema(description = "总创作数")
    @TableField(value = "total_articles")
    private Integer totalArticles;

    @Schema(description = "总获赞数")
    @TableField(value = "total_likes")
    private Integer totalLikes;

    @Schema(description = "总获收藏数")
    @TableField(value = "total_collects")
    private Integer totalCollects;

    @Schema(description = "总获评论数")
    @TableField(value = "total_comments")
    private Integer totalComments;

    @Schema(description = "总获分享数")
    @TableField(value = "total_shares")
    private Integer totalShares;

    @Schema(description = "粉丝数")
    @TableField(value = "follower_count")
    private Integer followerCount;

    @Schema(description = "关注数")
    @TableField(value = "follow_count")
    private Integer followCount;
}
