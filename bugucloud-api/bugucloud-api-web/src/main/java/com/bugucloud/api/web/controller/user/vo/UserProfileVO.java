package com.bugucloud.api.web.controller.user.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 功能描述: 用户个人主页VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/27 - 23:26
 */
@Data
public class UserProfileVO {

    /** 用户ID */
    private Long userId;

    /** 用户昵称 */
    private String nickname;

    /** 用户头像URL */
    private String avatar;

    /** IP属地 */
    private String ipLocation;

    /** 个性签名 */
    private String signature;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 个人简介 */
    private String personalIntro;

    /** 总访问量 */
    private Long totalViews;

    /** 总创作数 */
    private Integer totalArticles;

    /** 总粉丝数 */
    private Integer followerCount;

    /** 总关注数 */
    private Integer followCount;

    /** 是否被关注 */
    private Boolean isFollowed;

    /** 是否求更新 */
    private Boolean isRequestedUpdate;

    /** 个人成就 */
    private AchievementVO achievement;

    /** 该用户的文章列表 */
    private List<UserArticleVO> articles;
}
