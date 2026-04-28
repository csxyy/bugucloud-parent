package com.bugucloud.api.web.controller.article.vo;

import lombok.Data;

/**
 * 功能描述: 文章列表项VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/27 - 23:17
 */

@Data
public class ArticleItemVO {

    /** 用户ID */
    private Long userId;

    /** 用户头像URL */
    private String avatar;

    /** 用户昵称 */
    private String nickname;

    /** 文章ID */
    private Long articleId;

    /** 文章标题 */
    private String title;

    /** 文章摘要 */
    private String summary;

    /** 文章封面URL */
    private String cover;

    /** 阅读数 */
    private Long views;

    /** 点赞数 */
    private Integer likes;

    /** 收藏数 */
    private Integer collects;
}
