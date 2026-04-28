package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 功能描述: 用户关注关系表
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:12
 */
@TableName("t_user_follow")
@Data
@Schema(description = "用户关注关系表")
public class UserFollow extends BaseEntity {

    @Schema(description = "关注者ID(主动关注的人)")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "被关注者ID")
    @TableField(value = "followed_user_id")
    private Long followedUserId;

    @Schema(description = "冗余：被关注者昵称")
    @TableField(value = "followed_nickname")
    private String followedNickname;

    @Schema(description = "冗余：被关注者头像")
    @TableField(value = "followed_avatar")
    private String followedAvatar;

    @Schema(description = "关注来源 1=博客 2=主页")
    @TableField(value = "follow_source")
    private Integer followSource;

    @Schema(description = "是否取消 0=有效 1=已取消")
    @TableField(value = "is_cancel")
    private Integer isCancel;
}
