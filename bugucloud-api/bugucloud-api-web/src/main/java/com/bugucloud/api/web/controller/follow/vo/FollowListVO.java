package com.bugucloud.api.web.controller.follow.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 功能描述: 关注列表VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 9:56
 */
@Data
public class FollowListVO {

    /** 用户ID */
    private Long userId;

    /** 用户昵称 */
    private String nickname;

    /** 关注时间 */
    private LocalDateTime followTime;

    /** 对方是否关注我 */
    private Boolean isMutualFollow;
}
