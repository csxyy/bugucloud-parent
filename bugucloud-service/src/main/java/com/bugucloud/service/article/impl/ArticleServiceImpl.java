package com.bugucloud.service.article.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.core.entity.Article;
import com.bugucloud.core.entity.ArticleTag;
import com.bugucloud.core.mapper.ArticleMapper;
import com.bugucloud.core.vo.ArticleDetailVO;
import com.bugucloud.core.vo.ArticleItemVO;
import com.bugucloud.core.vo.ArticleManageVO;
import com.bugucloud.service.article.ArticleService;
import com.bugucloud.service.req.ArticleCreateReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能描述: 文章信息Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleMapper articleMapper;

    @Override
    public List<ArticleItemVO> getArticleListByTagId(Long tagId) {
        return articleMapper.selectArticleListByTagId(tagId);
    }

    @Override
    public ArticleDetailVO getArticleDetailById(Long articleId, Long currentUserId) {
        // 查询文章详情
        ArticleDetailVO detail = articleMapper.selectArticleDetailById(articleId, currentUserId);

        if (detail == null) {
            throw new BusinessException("文章不存在或未发布");
        }

        // 如果currentUserId为null，说明未登录，isLiked、isCollected、isFollowed已在SQL中处理为0
        // 这里可以根据业务需要，对未登录用户的这些字段强制设置为false
        if (currentUserId == null) {
            detail.setIsLiked(false);
            detail.setIsCollected(false);
            detail.setIsFollowed(false);
        }

        return detail;
    }

    @Override
    public List<ArticleManageVO> getArticleManageListByUserId(Long userId) {
        return articleMapper.selectArticleManageListByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createArticle(ArticleCreateReq req, Long userId) {
        // 1. 构建文章实体（ID由MyBatis-Plus自动生成）
        Article article = new Article();
        article.setUserId(userId);
        article.setTitle(req.getTitle());
        article.setSummary(req.getSummary());
        article.setCover(req.getCover());
        article.setContent(req.getContent());
        article.setIsPublished(req.getIsPublished() != null ? req.getIsPublished() : 0);
        article.setAuditStatus(0);
        article.setSort(0);
        article.setViews(0L);
        article.setLikes(0);
        article.setComments(0);
        article.setCollects(0);
        article.setShares(0);

        // 2. 使用自定义XML插入文章
        int insertCount = articleMapper.insertArticle(article);
        if (insertCount <= 0) {
            throw new BusinessException("文章创建失败");
        }

        // 3. 处理标签关联
        if (CollectionUtils.isNotEmpty(req.getTagIds())) {
            List<ArticleTag> articleTags = req.getTagIds().stream()
                    .map(tagId -> {
                        ArticleTag articleTag = new ArticleTag();
                        articleTag.setArticleId(article.getId());
                        articleTag.setTagId(tagId);
                        return articleTag;
                    })
                    .collect(Collectors.toList());

            // 使用自定义XML批量插入标签
            articleMapper.batchInsertArticleTags(articleTags);
        }

        return article.getId();
    }
}
