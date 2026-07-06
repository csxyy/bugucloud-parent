package com.bugucloud.core.mapper;

import com.bugucloud.core.vo.ArticleContentVO;
import org.apache.ibatis.annotations.Param;

/**
 * 功能描述: 文章内容
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/27 - 22:37
 */
public interface ArticleContentMapper {

    /**
     * 查询文章内容（文章信息 + 标签 + 作者简要信息）
     * @param articleId 文章ID
     * @param currentUserId 当前用户ID（可为null）
     * @return 文章内容
     */
    ArticleContentVO selectArticleContent(@Param("articleId") Long articleId,
                                          @Param("currentUserId") Long currentUserId);
}
