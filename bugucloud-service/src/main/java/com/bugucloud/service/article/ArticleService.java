package com.bugucloud.service.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bugucloud.core.dto.ArticleItemDTO;
import com.bugucloud.core.dto.ArticleManageDTO;
import com.bugucloud.core.entity.Article;
import com.bugucloud.service.req.ArticleCreateReq;

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
     * 根据文章ID查询文章详情
     */
    /**
     * 根据文章ID查询文章详情
     * @param articleId 文章ID
     * @param currentUserId 当前登录用户ID（可为null，表示未登录）
     * @return 文章详情DTO
     */
    com.bugucloud.core.dto.ArticleDetailDTO getArticleDetailById(Long articleId, Long currentUserId);

    /**
     * 根据标签ID查询文章列表
     * @param tagId 标签ID（为null时查询全部已发布文章）
     * @return 文章列表
     */
    List<ArticleItemDTO> getArticleListByTagId(Long tagId);

    /**
     * 根据用户ID查询文章管理列表（包含所有状态）
     * @param userId 用户ID
     * @return 文章管理列表
     */
    List<ArticleManageDTO> getArticleManageListByUserId(Long userId);

    /**
     * 新增文章
     * @param req 创建文章请求
     * @param userId 当前登录用户ID
     * @return 文章ID
     */
    Long createArticle(ArticleCreateReq req, Long userId);
}
