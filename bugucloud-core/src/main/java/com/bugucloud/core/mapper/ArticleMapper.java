package com.bugucloud.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugucloud.core.entity.Article;
import com.bugucloud.core.entity.ArticleTag;
import com.bugucloud.core.vo.ArticleItemVO;
import com.bugucloud.core.vo.ArticleManageVO;
import com.bugucloud.core.vo.AuthorOtherArticleVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
     * 根据标签ID查询文章列表（tagId为null时查询全部）
     */
    List<ArticleItemVO> selectArticleListByTagId(@Param("tagId") Long tagId);

    /**
     * 根据文章ID查询作者ID
     */
    @Select("SELECT user_id FROM t_article WHERE id = #{articleId} AND is_published = 1 " +
            "AND audit_status = 1")
    Long selectAuthorIdByArticleId(@Param("articleId") Long articleId);

    /**
     * 查询用户是否点赞该文章
     */
    @Select("SELECT COUNT(1) FROM t_article_like_collect " +
            "WHERE article_id = #{articleId} AND user_id = #{userId} " +
            "AND msg_type = 1 AND is_cancel = 0")
    Long selectUserLiked(@Param("articleId") Long articleId, @Param("userId") Long userId);

    /**
     * 查询用户是否收藏该文章
     */
    @Select("SELECT COUNT(1) FROM t_article_like_collect " +
            "WHERE article_id = #{articleId} AND user_id = #{userId} " +
            "AND msg_type = 2 AND is_cancel = 0")
    Long selectUserCollected(@Param("articleId") Long articleId, @Param("userId") Long userId);

    /**
     * 查询用户是否关注作者
     */
    @Select("SELECT COUNT(1) FROM t_user_follow " +
            "WHERE followed_user_id = #{authorId} AND user_id = #{userId} AND is_cancel = 0")
    Long selectUserFollowed(@Param("authorId") Long authorId, @Param("userId") Long userId);

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
