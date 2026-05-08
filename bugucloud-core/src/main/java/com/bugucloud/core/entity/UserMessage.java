package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 功能描述: 用户消息通知表
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:12
 */
@TableName("t_user_message")
@Data
@Schema(description = "用户消息通知表")
public class UserMessage extends BaseEntity {

    @Schema(description = "消息接收者ID")
    @TableField(value = "to_user_id")
    private Long toUserId;

    @Schema(description = "消息触发者ID")
    @TableField(value = "from_user_id")
    private Long fromUserId;

    @Schema(description = "冗余：触发者昵称")
    @TableField(value = "from_nickname")
    private String fromNickname;

    @Schema(description = "冗余：触发者头像")
    @TableField(value = "from_avatar")
    private String fromAvatar;

    @Schema(description = "消息类型 1=点赞 2=评论 3=回复 4=关注 5=收藏 6=崔更")
    @TableField(value = "msg_type")
    private Integer msgType;

    @Schema(description = "关联文章ID")
    @TableField(value = "article_id")
    private Long articleId;

    @Schema(description = "关联评论ID")
    @TableField(value = "comment_id")
    private Long commentId;

    @Schema(description = "消息内容")
    @TableField(value = "content")
    private String content;

    @Schema(description = "是否已读 0=未读 1=已读")
    @TableField(value = "is_read")
    private Integer isRead;

    @Schema(description = "是否开启邮箱通知 0=关闭 1=开启")
    @TableField(value = "email_notify")
    private Integer emailNotify;
}
