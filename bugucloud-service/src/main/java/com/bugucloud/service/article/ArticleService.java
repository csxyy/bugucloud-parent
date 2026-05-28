package com.bugucloud.service.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bugucloud.core.entity.Article;
import com.bugucloud.core.vo.*;
import com.bugucloud.service.req.ArticleCreateReq;
import com.bugucloud.service.req.ArticleLikeCollectReq;

import java.util.List;

/**
 * 功能描述: 文章信息Service
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
public interface ArticleService extends IService<Article> {

    /**
     * 根据标签ID查询文章列表
     * @param tagId 标签ID（为null时查询全部已发布文章）
     * @return 文章列表
     */
    List<ArticleItemVO> getArticleListByTagId(Long tagId);

    /**
     * 获取文章核心内容（包含标签、底部栏作者简要信息）
     */
    ArticleContentVO getArticleContent(Long articleId);

    /**
     * 获取文章作者详细信息（包含统计数据和其他文章）
     */
    ArticleAuthorDetailVO getArticleAuthor(Long articleId);

    /**
     * 获取当前用户对文章的交互状态（点赞/收藏/关注）
     * @param articleId 文章ID
     * @param userId 当前用户ID，可为null
     */
    ArticleInteractionVO getInteraction(Long articleId, Long userId);

    /**
     * 文章点赞/收藏
     * @param userId 当前用户ID
     * @param req 请求参数
     */
    void likeOrCollectArticle(Long userId, ArticleLikeCollectReq req);

    /**
     * 根据用户ID查询文章管理列表（包含所有状态）
     * @param userId 用户ID
     * @return 文章管理列表
     */
    List<ArticleManageVO> getArticleManageListByUserId(Long userId);

    /**
     * 新增文章
     * @param req 创建文章请求
     * @param userId 当前登录用户ID
     * @return 文章ID
     */
    Long createArticle(ArticleCreateReq req, Long userId);
}
