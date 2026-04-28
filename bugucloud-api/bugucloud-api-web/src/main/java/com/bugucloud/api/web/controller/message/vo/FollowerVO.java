package com.bugucloud.api.web.controller.message.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 功能描述: 新增粉丝VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 10:26
 */
@Data
public class FollowerVO {

    /** 用户ID */
    private Long userId;

    /** 用户头像 */
    private String avatar;

    /** 关注时间 */
    private LocalDateTime followTime;

    /** 我是否回关 */
    private Boolean isMutualFollow;
}
