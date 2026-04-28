package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 功能描述: 评论点赞记录表
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:12
 */
@TableName("t_comment_like")
@Data
@Schema(description = "评论点赞记录表")
public class CommentLike extends BaseEntity {

    @Schema(description = "点赞用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "关联评论ID")
    @TableField(value = "comment_id")
    private Long commentId;

    @Schema(description = "是否取消 0=有效 1=已取消")
    @TableField(value = "is_cancel")
    private Integer isCancel;
}
