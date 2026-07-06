package com.bugucloud.service.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
     * 根据标签ID分页查询文章列表
     * @param tagId 标签ID（可为null）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    IPage<ArticleItemVO> getArticleListByTagId(Long tagId, Integer pageNum, Integer pageSize);

    /**
     * 获取文章核心内容（包含标签、底部栏作者简要信息）
     * @param articleId 文章ID
     * @param currentUserId 当前用户ID（可为null）
     * @return 文章内容
     */
    ArticleContentVO getArticleContent(Long articleId, Long currentUserId);

    /**
     * 获取文章作者详细信息（包含统计数据和其他文章）
     * @param articleId 文章ID
     * @param currentUserId 当前用户ID（可为null）
     * @return 作者详情
     */
    ArticleAuthorDetailVO getArticleAuthor(Long articleId, Long currentUserId);

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
     * 查询文章管理列表
     * @param userId 当前用户ID
     * @param keyword 搜索关键字（可为null）
     * @param isPublished 发布状态（可为null）
     * @return 文章管理列表
     */
    List<ArticleManageVO> getArticleManageList(Long userId, String keyword, Integer isPublished);

    /**
     * 新增/更新文章
     * @param req 创建文章请求
     * @param userId 当前登录用户ID
     * @return 文章ID
     */
    Long saveOrUpdateArticle(ArticleCreateReq req, Long userId);

    /**
     * 查询文章编辑数据
     * @param articleId 文章ID
     * @return 文章编辑数据
     */
    ArticleEditVO getArticleEditById(Long articleId);

    /**
     * 删除文章
     * @param articleId 文章ID
     * @param userId 当前用户ID
     */
    void deleteArticle(Long articleId, Long userId);
}
