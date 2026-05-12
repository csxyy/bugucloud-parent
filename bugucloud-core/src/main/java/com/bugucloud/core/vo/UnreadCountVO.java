package com.bugucloud.core.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 功能描述: 未读数量统计
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/11 - 23:53
 */

@Data
public class UnreadCountVO {

    /** 消息类型 1=获赞和收藏 2=粉丝 3=评论 4=系统通知 */
    private Integer msgType;

    /** 未读数量 */
    private Long unreadCount;
}
