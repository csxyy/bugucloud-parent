package com.bugucloud.core.mapper;

import com.bugucloud.core.vo.ArticleAuthorDetailVO;
import com.bugucloud.core.vo.AuthorOtherArticleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能描述: 作者信息 Mapper（XML）
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/27 - 22:38
 */
public interface ArticleAuthorMapper {

    /**
     * 查询作者详细信息（用户信息 + 统计数据）
     */
    ArticleAuthorDetailVO selectAuthorDetail(@Param("authorId") Long authorId);

    /**
     * 查询作者其他文章（排除当前文章，最新5篇）
     */
    List<AuthorOtherArticleVO> selectAuthorOtherArticles(@Param("authorId") Long authorId,
                                                         @Param("excludeArticleId") Long excludeArticleId);
}
