package com.bugucloud.api.web.controller.user.vo;

import lombok.Data;

/**
 * 功能描述: 个人成就VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/27 - 23:26
 */

@Data
public class AchievementVO {

    /** 内容总获得点赞数 */
    private Integer totalLikes;

    /** 总获得评论数 */
    private Integer totalComments;

    /** 总获得收藏数 */
    private Integer totalCollects;

    /** 总获得分享数 */
    private Integer totalShares;
}
