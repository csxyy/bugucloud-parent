package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 功能描述: 用户信息表
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:12
 */
@TableName("t_user")
@Data
@Schema(description = "用户信息表")
public class User extends BaseEntity {

    @Schema(description = "登录用户名（唯一）")
    @TableField(value = "username")
    private String username;

    @Schema(description = "上次修改用户名时间")
    @TableField(value = "username_update_time")
    private LocalDateTime usernameUpdateTime;

    @Schema(description = "加密密码")
    @TableField(value = "password")
    private String password;

    @Schema(description = "昵称(可重复)")
    @TableField(value = "nickname")
    private String nickname;

    @Schema(description = "头像URL")
    @TableField(value = "avatar")
    private String avatar;

    @Schema(description = "登录邮箱(唯一)")
    @TableField(value = "email")
    private String email;

    @Schema(description = "是否开启邮箱通知 0=关闭 1=开启")
    @TableField(value = "email_notify")
    private Integer emailNotify;

    @Schema(description = "个人简介")
    @TableField(value = "personal_intro")
    private String personalIntro;

    @Schema(description = "IP属地-国家")
    @TableField(value = "country")
    private String country;

    @Schema(description = "IP属地-省份")
    @TableField(value = "province")
    private String province;

    @Schema(description = "注册IP")
    @TableField(value = "register_ip")
    private String registerIp;

    @Schema(description = "最后登录IP")
    @TableField(value = "last_login_ip")
    private String lastLoginIp;

    @Schema(description = "最后登录时间")
    @TableField(value = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Schema(description = "身份标识 0=普通用户 1=会员 2=管理员")
    @TableField(value = "role")
    private Integer role;

    @Schema(description = "会员生效时间")
    @TableField(value = "vip_start_time")
    private LocalDateTime vipStartTime;

    @Schema(description = "会员过期时间")
    @TableField(value = "vip_expire_time")
    private LocalDateTime vipExpireTime;

    @Schema(description = "账号状态 1-正常 0-禁用/拉黑 -1-注销")
    @TableField(value = "status")
    private Integer status;
}
