package com.bugucloud.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.common.enums.FileTypeEnum;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.core.entity.*;
import com.bugucloud.core.mapper.*;
import com.bugucloud.core.vo.*;
import com.bugucloud.service.article.ArticleService;
import com.bugucloud.service.async.AsyncTaskService;
import com.bugucloud.service.file.FileMetadataService;
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

    private final AsyncTaskService asyncTaskService;

    private final FileMetadataService fileMetadataService;


    @Override
    public IPage<ArticleItemVO> getArticleListByTagId(Long tagId, Integer pageNum, Integer pageSize) {
        // 创建分页对象，默认pageNum=1, pageSize=8
        Page<ArticleItemVO> page = new Page<>(pageNum, pageSize);

        // 分页查询
        IPage<ArticleItemVO> result = articleMapper.selectArticleListByTagId(page, tagId);

        return result;
    }

    // ========== 接口1：文章核心内容 ==========
    @Override
    public ArticleContentVO getArticleContent(Long articleId, Long currentUserId) {
        ArticleContentVO vo = articleContentMapper.selectArticleContent(articleId, currentUserId);
        if (vo == null) {
            throw new BusinessException("文章不存在或已下架");
        }
        return vo;
    }

    // ========== 接口2：作者详情 + 侧边栏 ==========
    @Override
    public ArticleAuthorDetailVO getArticleAuthor(Long articleId, Long currentUserId) {
        // 先查出文章对应的作者ID（作者自己的文章不受限制）
        Long authorId = articleMapper.selectAuthorIdByArticleId(articleId, currentUserId);
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
        Long authorId = articleMapper.selectAuthorIdByArticleId(articleId, userId);
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
    public Long saveOrUpdateArticle(ArticleCreateReq req, Long userId) {
        // 判断是新增还是更新
        if (req.getArticleId() != null) {
            // 更新文章
            return updateArticle(req, userId);
        } else {
            // 新增文章
            return createArticle(req, userId);
        }
    }

    /**
     * 新增文章
     */
    private Long createArticle(ArticleCreateReq req, Long userId) {
        // 0. 获取封面关联ID
        Long tempRelatedId = null;
        String coverUrl = req.getCover();

        if (StringUtils.isBlank(coverUrl)) {
            tempRelatedId = fileMetadataService.getLatestCoverRelatedId(userId);
            if (tempRelatedId != null) {
                LambdaQueryWrapper<FileMetadata> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(FileMetadata::getUploaderId, userId)
                        .eq(FileMetadata::getFileType, FileTypeEnum.ARTICLE_COVER)
                        .eq(FileMetadata::getRelatedId, tempRelatedId)
                        .eq(FileMetadata::getIsDeleted, 0)
                        .last("LIMIT 1");
                FileMetadata coverFile = fileMetadataService.getOne(wrapper);
                if (coverFile != null) {
                    coverUrl = coverFile.getFileUrl();
                    log.info("从文件元数据中获取到封面URL: {}", coverUrl);
                }
            }
        }

        // 1. 构建文章实体
        Article article = new Article();

        if (tempRelatedId != null) {
            article.setId(tempRelatedId);
            log.info("使用文件元数据的related_id作为文章ID: {}", tempRelatedId);
        }

        article.setUserId(userId);
        article.setTitle(req.getTitle());
        article.setSummary(getSummary(req.getSummary(), req.getContent()));
        article.setCover(coverUrl);
        article.setContent(req.getContent());
        article.setIsPublished(req.getIsPublished() != null ? req.getIsPublished() : 0);
        article.setAuditStatus(getAuditStatus(req.getIsPublished()));
        article.setSort(0);
        article.setViews(0L);
        article.setLikes(0);
        article.setComments(0);
        article.setCollects(0);
        article.setShares(0);

        // 2. 插入文章
        int insertCount = articleMapper.insertArticle(article);
        if (insertCount <= 0) {
            throw new BusinessException("文章创建失败");
        }

        // 3. 处理标签关联
        if (CollectionUtils.isNotEmpty(req.getTagIds())) {
            batchInsertTags(article.getId(), req.getTagIds());
        }

        log.info("文章创建成功, 文章ID: {}, 封面URL: {}", article.getId(), coverUrl);
        return article.getId();
    }

    /**
     * 更新文章
     */
    private Long updateArticle(ArticleCreateReq req, Long userId) {
        // 1. 查询原文章
        Article article = articleMapper.selectById(req.getArticleId());
        if (article == null) {
            throw new BusinessException("文章不存在");
        }

        // 2. 权限校验：只能编辑自己的文章
        if (!article.getUserId().equals(userId)) {
            throw new BusinessException("无权编辑他人文章");
        }

        // 3. 更新文章信息
        article.setTitle(req.getTitle());
        article.setSummary(getSummary(req.getSummary(), req.getContent()));
        article.setCover(req.getCover());
        article.setContent(req.getContent());
        article.setIsPublished(req.getIsPublished() != null ? req.getIsPublished() : article.getIsPublished());
        article.setAuditStatus(getAuditStatus(req.getIsPublished()));

        // 4. 更新文章
        int updateCount = articleMapper.updateArticle(article);
        if (updateCount <= 0) {
            throw new BusinessException("文章更新失败");
        }

        // 5. 处理标签关联（先删除旧标签，再插入新标签）
        articleMapper.deleteArticleTags(article.getId());
        if (CollectionUtils.isNotEmpty(req.getTagIds())) {
            batchInsertTags(article.getId(), req.getTagIds());
        }

        log.info("文章更新成功, 文章ID: {}", article.getId());
        return article.getId();
    }

    /**
     * 批量插入标签
     */
    private void batchInsertTags(Long articleId, List<Long> tagIds) {
        List<ArticleTag> articleTags = tagIds.stream()
                .map(tagId -> {
                    ArticleTag articleTag = new ArticleTag();
                    articleTag.setArticleId(articleId);
                    articleTag.setTagId(tagId);
                    return articleTag;
                })
                .collect(Collectors.toList());

        articleMapper.batchInsertArticleTags(articleTags);
    }

    /**
     * 获取文章摘要
     * 如果摘要为空或空字符串，则取正文前256个字
     *
     * @param summary 前端传入的摘要
     * @param content 文章正文
     * @return 摘要内容
     */
    private String getSummary(String summary, String content) {
        // 如果摘要不为空，直接返回
        if (StringUtils.isNotBlank(summary)) {
            return summary;
        }

        // 摘要为空，从正文中截取前256个字
        if (StringUtils.isNotBlank(content)) {
            // 去除HTML标签（如果内容包含HTML）
            String plainText = content.replaceAll("<[^>]+>", "");
            // 去除多余空白字符
            plainText = plainText.replaceAll("\\s+", " ").trim();

            // 截取前256个字
            if (plainText.length() > 256) {
                return plainText.substring(0, 256);
            }
            return plainText;
        }

        // 正文也为空，返回空字符串
        return "";
    }

    /**
     * 根据发布状态获取审核状态
     *
     * @param isPublished 发布状态 0-草稿 1-已发布
     * @return 审核状态 0-未提交 1-审核中
     */
    private Integer getAuditStatus(Integer isPublished) {
        if (isPublished == null || isPublished == 0) {
            // 草稿：未提交审核
            return 0;
        } else if (isPublished == 1) {
            // 已发布：审核中
            return 1;
        }
        // 其他状态默认未提交
        return 0;
    }


    @Override
    public ArticleEditVO getArticleEditById(Long articleId) {
        ArticleEditVO articleEdit = articleMapper.selectArticleEditById(articleId);
        if (articleEdit == null) {
            throw new BusinessException("文章不存在");
        }
        return articleEdit;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(Long articleId, Long userId) {
        // 1. 查询文章是否存在
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }

        // 2. 权限校验：只能删除自己的文章
        if (!article.getUserId().equals(userId)) {
            throw new BusinessException("无权删除他人文章");
        }

        // 3. 删除标签关联
        articleMapper.deleteArticleTags(articleId);

        // 4. 删除文章
        articleMapper.deleteArticle(articleId);

        log.info("文章删除成功，文章ID：{}，用户ID：{}", articleId, userId);
    }
}
