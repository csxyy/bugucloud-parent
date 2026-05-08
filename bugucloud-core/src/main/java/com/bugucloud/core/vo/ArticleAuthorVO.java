package com.bugucloud.core.vo;

import lombok.Data;
import java.util.List;

/**
 * 功能描述: DTO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 14:44
 */
@Data
public class ArticleAuthorVO {
    /** 用户ID */
    private Long id;

    /** 用户昵称 */
    private String nickname;

    /** 用户头像URL */
    private String avatar;

    /** 用户个人简介 */
    private String personalIntro;

    /** 身份标识 0=普通用户 1=会员 2=管理员 */
    private Integer role;

    // ===============用户统计数据================
    /** 总原创数 */
    private Integer totalArticles;

    /** 总获赞数 */
    private Integer totalLikes;

    /** 总获收藏数 */
    private Integer totalCollects;

    /** 粉丝数 */
    private Integer followerCount;

    /** 该用户的其他文章 */
    private List<AuthorOtherArticleVO> otherArticles;
}
