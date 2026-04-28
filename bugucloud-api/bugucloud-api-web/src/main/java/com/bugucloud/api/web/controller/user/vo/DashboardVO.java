package com.bugucloud.api.web.controller.user.vo;

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

    /** 会员等级 */
    private Integer role;

    /** 会员生效时间 */
    private LocalDateTime vipStartTime;

    /** 会员到期时间 */
    private LocalDateTime vipExpireTime;

    /** 消息通知是否开启 */
    private Boolean notificationEnabled;
}
