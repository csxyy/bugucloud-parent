package com.bugucloud.core.vo;

import lombok.Data;

/**
 * 功能描述: 用户交互状态 VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/27 - 22:27
 */
@Data
public class ArticleInteractionVO {
    /** 文章ID */
    private Long articleId;

    /** 当前用户是否点赞 */
    private Boolean isLiked;

    /** 当前用户是否收藏 */
    private Boolean isCollected;

    /** 当前用户是否关注了作者 */
    private Boolean isFollowedAuthor;

    /** 是否为自己的文章 */
    private Boolean isSelf;
}
