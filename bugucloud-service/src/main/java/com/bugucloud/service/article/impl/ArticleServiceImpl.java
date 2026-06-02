package com.bugucloud.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.core.entity.*;
import com.bugucloud.core.mapper.*;
import com.bugucloud.core.vo.*;
import com.bugucloud.service.article.ArticleService;
import com.bugucloud.service.async.AsyncTaskService;
import com.bugucloud.service.req.ArticleCreateReq;
import com.bugucloud.service.req.ArticleLikeCollectReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

    private final ArticleLikeCollectMapper articleLikeCollectMapper;

    private final UserMapper userMapper;

    private final AsyncTaskService asyncTaskService;


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

        // 4. 判断是否为自己的文章
        vo.setIsSelf(userId.equals(authorId));

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeOrCollectArticle(Long userId, ArticleLikeCollectReq req) {
        // 1. 查询文章是否存在
        Article article = articleMapper.selectById(req.getArticleId());
        if (article == null) {
            throw new BusinessException("文章不存在");
        }

        // 2. 查询是否已有操作记录
        ArticleLikeCollect likeCollect = articleLikeCollectMapper.selectOne(
                new LambdaQueryWrapper<ArticleLikeCollect>()
                        .eq(ArticleLikeCollect::getUserId, userId)
                        .eq(ArticleLikeCollect::getArticleId, req.getArticleId())
                        .eq(ArticleLikeCollect::getMsgType, req.getMsgType())
        );

        if (likeCollect == null) {
            // 3. 第一次操作
            if (req.getIsActive()) {
                // 3.1 触发操作：创建记录
                likeCollect = new ArticleLikeCollect();
                likeCollect.setUserId(userId);
                likeCollect.setArticleId(req.getArticleId());
                likeCollect.setArticleTitle(article.getTitle());
                likeCollect.setTargetUserId(article.getUserId());
                likeCollect.setMsgType(req.getMsgType());
                likeCollect.setIsCancel(0);

                // 冗余用户头像
                User user = userMapper.selectById(userId);
                if (user != null) {
                    likeCollect.setUserAvatar(user.getAvatar());
                }

                articleLikeCollectMapper.insert(likeCollect);

                // 异步更新统计
                asyncTaskService.updateArticleStats(req.getArticleId(), req.getMsgType(), true);
                asyncTaskService.updateUserStats(article.getUserId(), req.getMsgType(), true);

                // 异步发送通知（如果不是作者自己）
                if (!userId.equals(article.getUserId())) {
                    asyncTaskService.sendLikeCollectNotification(likeCollect, article.getUserId());
                }
            }
        } else {
            // 4. 已有记录，根据操作类型和当前状态处理
            if (req.getIsActive()) {
                // 4.1 触发操作
                if (likeCollect.getIsCancel() == 1) {
                    // 之前取消过，重新触发
                    likeCollect.setIsCancel(0);
                    articleLikeCollectMapper.updateById(likeCollect);

                    // 异步更新统计
                    asyncTaskService.updateArticleStats(req.getArticleId(), req.getMsgType(), true);
                    asyncTaskService.updateUserStats(article.getUserId(), req.getMsgType(), true);
                }
            } else {
                // 4.2 取消操作
                if (likeCollect.getIsCancel() == 0) {
                    // 取消操作
                    likeCollect.setIsCancel(1);
                    articleLikeCollectMapper.updateById(likeCollect);

                    // 异步更新统计
                    asyncTaskService.updateArticleStats(req.getArticleId(), req.getMsgType(), false);
                    asyncTaskService.updateUserStats(article.getUserId(), req.getMsgType(), false);
                }
            }
        }
    }

    @Override
    public List<ArticleManageVO> getArticleManageList(Long userId, String keyword, Integer isPublished) {
        List<ArticleManageVO> list =
                articleMapper.selectArticleManageList(userId, keyword, isPublished);
        return list != null ? list : Collections.emptyList();
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
