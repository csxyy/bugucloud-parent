package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 功能描述: 互动消息未读状态实体类
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/11 - 22:53
 */

@Data
@TableName("t_interaction_msg_unread")
@Schema(description = "互动消息未读状态实体类")
public class InteractionMessage extends BaseEntity {

    @Schema(description = "消息接收者ID")
    @TableField(value = "to_user_id")
    private Long toUserId;

    @Schema(description = "消息触发者ID")
    @TableField(value = "from_user_id")
    private Long fromUserId;

    @Schema(description = "消息类型 1=获赞和收藏 2=粉丝 3=评论 4=系统通知")
    @TableField(value = "msg_type")
    private Integer msgType;

    @Schema(description = "业务主键ID（对应各业务表的主键）")
    @TableField(value = "business_id")
    private Long businessId;

    @Schema(description = "是否已读 0=未读 1=已读")
    @TableField(value = "is_read")
    private Integer isRead;

}
