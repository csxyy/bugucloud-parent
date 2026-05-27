package com.bugucloud.service.article.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.core.entity.Article;
import com.bugucloud.core.entity.ArticleTag;
import com.bugucloud.core.mapper.ArticleAuthorMapper;
import com.bugucloud.core.mapper.ArticleContentMapper;
import com.bugucloud.core.mapper.ArticleMapper;
import com.bugucloud.core.vo.*;
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

    private final ArticleContentMapper articleContentMapper;

    private final ArticleAuthorMapper articleAuthorMapper;


    @Override
    public List<ArticleItemVO> getArticleListByTagId(Long tagId) {
        return articleMapper.selectArticleListByTagId(tagId);
    }

    // ========== 接口1：文章核心内容 ==========
    @Override
    public ArticleContentVO getArticleContent(Long articleId) {
        ArticleContentVO vo = articleContentMapper.selectArticleContent(articleId);
        if (vo == null) {
            throw new BusinessException("文章不存在或未发布");
        }
        return vo;
    }

    // ========== 接口2：作者详情 + 侧边栏 ==========
    @Override
    public ArticleAuthorDetailVO getArticleAuthor(Long articleId) {
        // 先查出文章对应的作者ID
        Long authorId = articleMapper.selectAuthorIdByArticleId(articleId);
        if (authorId == null) {
            throw new BusinessException("文章不存在或未发布");
        }

        // 查询作者详情
        ArticleAuthorDetailVO vo = articleAuthorMapper.selectAuthorDetail(authorId);
        if (vo == null) {
            throw new BusinessException("作者信息不存在");
        }

        // 查询作者其他文章（排除当前文章，最新5篇）
        List<AuthorOtherArticleVO> otherArticles = articleAuthorMapper
                .selectAuthorOtherArticles(authorId, articleId);
        vo.setOtherArticles(otherArticles);

        return vo;
    }

    // ========== 接口3：用户交互状态 ==========
    @Override
    public ArticleInteractionVO getInteraction(Long articleId, Long userId) {
        ArticleInteractionVO vo = new ArticleInteractionVO();
        vo.setArticleId(articleId);

        // 未登录用户全部返回 false
        if (userId == null) {
            vo.setIsLiked(false);
            vo.setIsCollected(false);
            vo.setIsFollowedAuthor(false);
            return vo;
        }

        // 1. 查询是否点赞
        Long likedCount = articleMapper.selectUserLiked(articleId, userId);
        vo.setIsLiked(likedCount != null && likedCount > 0);

        // 2. 查询是否收藏
        Long collectedCount = articleMapper.selectUserCollected(articleId, userId);
        vo.setIsCollected(collectedCount != null && collectedCount > 0);

        // 3. 查询是否关注作者
        Long authorId = articleMapper.selectAuthorIdByArticleId(articleId);
        Long followedCount = articleMapper.selectUserFollowed(authorId, userId);
        vo.setIsFollowedAuthor(followedCount != null && followedCount > 0);

        return vo;
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
