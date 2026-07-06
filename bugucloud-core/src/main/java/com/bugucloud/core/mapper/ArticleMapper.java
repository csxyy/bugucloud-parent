package com.bugucloud.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bugucloud.core.entity.Article;
import com.bugucloud.core.entity.ArticleTag;
import com.bugucloud.core.vo.ArticleEditVO;
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
     * 根据标签ID分页查询文章列表
     * @param page 分页对象
     * @param tagId 标签ID（可为null）
     * @return 分页结果
     */
    IPage<ArticleItemVO> selectArticleListByTagId(Page<ArticleItemVO> page,
                                                  @Param("tagId") Long tagId);

    /**
     * 根据文章ID查询作者ID（作者自己的文章不受发布状态限制）
     */
    @Select("SELECT user_id FROM t_article WHERE id = #{articleId} " +
            "AND ( (is_published = 1 AND audit_status = 1) OR user_id = #{currentUserId} )")
    Long selectAuthorIdByArticleId(@Param("articleId") Long articleId,
                                   @Param("currentUserId") Long currentUserId);

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
     * 根据用户ID和条件查询文章管理列表
     */
    List<ArticleManageVO> selectArticleManageList(@Param("userId") Long userId,
                                                  @Param("keyword") String keyword,
                                                  @Param("isPublished") Integer isPublished);

    /**
     * 插入文章记录
     */
    int insertArticle(Article article);

    /**
     * 批量插入文章标签关联
     */
    int batchInsertArticleTags(@Param("articleTags") List<ArticleTag> articleTags);

    /**
     * 更新文章记录
     */
    int updateArticle(Article article);

    /**
     * 删除文章所有标签关联
     */
    int deleteArticleTags(@Param("articleId") Long articleId);

    /**
     * 根据文章ID查询编辑所需数据
     */
    ArticleEditVO selectArticleEditById(@Param("articleId") Long articleId);


    /**
     * 删除文章
     */
    int deleteArticle(@Param("articleId") Long articleId);
}
