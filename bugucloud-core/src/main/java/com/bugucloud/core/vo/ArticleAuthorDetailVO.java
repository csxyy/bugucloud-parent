package com.bugucloud.core.vo;

import lombok.Data;

import java.util.List;

/**
 * 功能描述: 作者详情 + 侧边栏 VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/27 - 22:27
 */
@Data
public class ArticleAuthorDetailVO {
    /** 用户ID */
    private Long id;

    /** 用户昵称 */
    private String nickname;

    /** 用户头像URL */
    private String avatar;

    /** 用户个人简介 */
    private String personalIntro;

    /** 身份标识 0=普通用户 1=会员 2=超级会员 3=管理员 */
    private Integer role;

    /** 总原创数 */
    private Integer totalArticles;

    /** 总获赞数 */
    private Integer totalLikes;

    /** 总获收藏数 */
    private Integer totalCollects;

    /** 粉丝数 */
    private Integer followerCount;

    /** 该用户的其他文章（排除当前文章，最新5篇） */
    private List<AuthorOtherArticleVO> otherArticles;
}
