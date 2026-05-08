package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 功能描述: 关注用户-求更新记录表
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:12
 */
@TableName("t_user_follow_request")
@Data
@Schema(description = "关注用户-求更新记录表")
public class UserFollowRequest extends BaseEntity {

    @Schema(description = "求更新用户ID（关注者）")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "被求更新博主ID（被关注者）")
    @TableField(value = "followed_user_id")
    private Long followedUserId;

    @Schema(description = "求更新时间")
    @TableField(value = "request_time")
    private LocalDateTime requestTime;

    // 标记数据库中不存在的字段
    @TableField(exist = false)
    private Date createTime;

    @TableField(exist = false)
    private Date updateTime;
}
