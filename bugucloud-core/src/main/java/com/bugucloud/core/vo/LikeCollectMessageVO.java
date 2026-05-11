package com.bugucloud.core.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能描述: 赞和收藏消息VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 10:26
 */

@Data
public class LikeCollectMessageVO {

    /** 用户ID */
    private Long userId;

    /** 用户头像 */
    private String avatar;

    /** 文章ID */
    private Long articleId;

    /** 文章标题 */
    private String articleTitle;

    /** 消息类型 1-赞 2-收藏 */
    private Integer msgType;

    /** 创建时间 */
    private LocalDateTime createTime;
}
