package com.bugucloud.core.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能描述: 控制台信息VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 9:49
 */
@Data
public class DashboardVO {

    /** 会员等级 0-普通用户 1-会员 2-超级会员 3=管理员 */
    private Integer role;

    /** 会员生效时间 */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime vipStartTime;

    /** 会员到期时间 */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime vipExpireTime;

    /** 会员剩余天数（未过期时有效，已过期返回0） */
    private Long vipRemainingDays;

    /** 消息通知是否开启 */
    private Boolean notificationEnabled;
}
