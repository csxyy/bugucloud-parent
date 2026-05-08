package com.bugucloud.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugucloud.core.entity.Article;
import com.bugucloud.core.entity.ArticleTag;
import com.bugucloud.core.vo.ArticleDetailVO;
import com.bugucloud.core.vo.ArticleItemVO;
import com.bugucloud.core.vo.ArticleManageVO;
import com.bugucloud.core.vo.AuthorOtherArticleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能描述: 文章信息Mapper
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:39
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 根据文章ID查询文章详情（包含作者信息、标签等）
     */
    ArticleDetailVO selectArticleDetailById(@Param("articleId") Long articleId,
                                            @Param("currentUserId") Long currentUserId);

    /**
     * 查询作者的其他文章（最新5篇）
     */
    List<AuthorOtherArticleVO> selectAuthorOtherArticles(@Param("userId") Long userId,
                                                         @Param("excludeArticleId") Long excludeArticleId);

    /**
     * 根据标签ID查询文章列表（tagId为null时查询全部）
     */
    List<ArticleItemVO> selectArticleListByTagId(@Param("tagId") Long tagId);

    /**
     * 根据用户ID查询文章管理列表（包含所有状态的文章）
     */
    List<ArticleManageVO> selectArticleManageListByUserId(@Param("userId") Long userId);

    /**
     * 插入文章记录
     */
    int insertArticle(Article article);

    /**
     * 批量插入文章标签关联
     */
    int batchInsertArticleTags(@Param("articleTags") List<ArticleTag> articleTags);
}
