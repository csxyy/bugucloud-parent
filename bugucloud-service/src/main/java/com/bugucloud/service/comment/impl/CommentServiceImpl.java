package com.bugucloud.service.comment.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.core.entity.*;
import com.bugucloud.core.mapper.*;
import com.bugucloud.core.vo.MineCommentVO;
import com.bugucloud.core.vo.ParentCommentVO;
import com.bugucloud.core.vo.SubCommentVO;
import com.bugucloud.service.comment.CommentNotificationService;
import com.bugucloud.service.comment.CommentService;
import com.bugucloud.service.req.CommentCreateReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能描述: 文章评论Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final CommentMapper commentMapper;
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;
    private final CommentNotificationService commentNotificationService;
    private final CommentLikeMapper commentLikeMapper;

    @Override
    public List<ParentCommentVO> getParentComments(Long articleId, Long currentUserId) {
        // 1. 校验文章是否存在
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }

        // 2. 校验文章是否已删除（is_published=2表示下架/删除）
        if (article.getIsPublished() == 2) {
            throw new BusinessException("文章已下架");
        }

        // 3. 校验文章是否审核通过（如果需要的话）
        if (article.getAuditStatus() != 1) {
            throw new BusinessException("文章审核未通过，无法查看评论");
        }

        // 4. 查询一级评论列表
        List<ParentCommentVO> parentComments = commentMapper.selectParentComments(articleId, currentUserId);
        return parentComments != null ? parentComments : Collections.emptyList();
    }

    @Override
    public List<SubCommentVO> getChildComments(Long rootId, Long currentUserId) {
        // 1. 校验根评论是否存在
        Comment rootComment = commentMapper.selectById(rootId);
        if (rootComment == null) {
            throw new BusinessException("评论不存在");
        }

        // 2. 校验根评论是否已删除
        if (rootComment.getIsDeleted() == 1) {
            throw new BusinessException("评论已删除");
        }

        // 3. 校验根评论是否为一级评论（rootId对应的一级评论的root_id应该为0）
        if (rootComment.getRootId() != 0) {
            throw new BusinessException("只能查询一级评论的子评论");
        }

        // 4. 校验关联文章是否存在
        Article article = articleMapper.selectById(rootComment.getArticleId());
        if (article == null) {
            throw new BusinessException("关联文章不存在");
        }

        // 5. 校验文章是否已下架
        if (article.getIsPublished() == 2) {
            throw new BusinessException("关联文章已下架");
        }

        // 6. 查询子评论
        List<SubCommentVO> childComments = commentMapper.selectChildComments(rootId, currentUserId);
        return childComments != null ? childComments : Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createComment(CommentCreateReq req, Long userId) {
        // 1. 查询文章信息（获取文章作者ID）
        Article article = articleMapper.selectById(req.getArticleId());
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        if (article.getIsPublished() != 1 || article.getAuditStatus() != 1) {
            throw new BusinessException("文章未发布或审核未通过，无法评论");
        }

        // 2. 查询当前用户信息
        User user = userMapper.selectById(userId);
        if (user == null || user.getStatus() != 1) {
            throw new BusinessException("用户不存在或已被禁用");
        }

        // 3. 获取rootId和parentId（默认0表示一级评论）
        Long rootId = req.getRootId() != null ? req.getRootId() : 0L;
        Long parentId = req.getParentId() != null ? req.getParentId() : 0L;

        // 4. 如果是子评论，校验父评论
        if (parentId != 0L) {
            Comment parentComment = commentMapper.selectById(parentId);
            if (parentComment == null || parentComment.getIsDeleted() == 1) {
                throw new BusinessException("父评论不存在或已删除");
            }

            // 如果前端没传rootId，从父评论中获取
            if (rootId == 0L) {
                // 父评论是一级评论，rootId就是父评论ID
                if (parentComment.getRootId() == 0) {
                    rootId = parentComment.getId();
                } else {
                    // 父评论是子评论，rootId是父评论的rootId
                    rootId = parentComment.getRootId();
                }
            }
        }

        // 5. 构建评论实体
        Comment comment = new Comment();
        comment.setArticleId(req.getArticleId());
        comment.setUserId(userId);
        comment.setNickname(user.getNickname());
        comment.setAvatar(user.getAvatar());
        comment.setContent(req.getContent());
        comment.setRootId(rootId);
        comment.setParentId(parentId);
        comment.setLikes(0);
        comment.setSubCommentCount(0);
        comment.setIsDeleted(0);

        // 6. 处理回复逻辑
        if (parentId != 0L) {
            // 查询父评论信息（已在第4步查询过，避免重复查询）
            Comment parentComment = commentMapper.selectById(parentId);

            // 设置被回复用户信息
            Long replyUserId = req.getReplyUserId() != null ? req.getReplyUserId() : parentComment.getUserId();
            comment.setReplyUserId(replyUserId);

            // 查询被回复用户昵称
            User replyUser = userMapper.selectById(replyUserId);
            if (replyUser != null) {
                comment.setReplyNickname(replyUser.getNickname());
            }
        } else {
            comment.setReplyUserId(0L);
            comment.setReplyNickname(null);
        }

        // 7. 插入评论
        int insertCount = commentMapper.insertComment(comment);
        if (insertCount <= 0) {
            throw new BusinessException("评论创建失败");
        }

        // 8. 更新一级评论的子评论数
        if (rootId != 0L) {
            commentMapper.update(null,
                    new LambdaUpdateWrapper<Comment>()
                            .eq(Comment::getId, rootId)
                            .setSql("sub_comment_count = sub_comment_count + 1")
            );
        }

        // 9. 更新文章评论数
        articleMapper.update(null,
                new LambdaUpdateWrapper<Article>()
                        .eq(Article::getId, req.getArticleId())
                        .setSql("comments = comments + 1")
        );

        // TODO: 发送消息通知
        // commentNotificationService.sendCommentNotifications(comment, article, userId, parentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeComment(Long commentId, Long userId) {
        // 1. 查询评论是否存在
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() == 1) {
            throw new BusinessException("评论不存在或已删除");
        }

        // 2. 查询是否有点赞记录
        CommentLike commentLike = commentLikeMapper.selectOne(
                new LambdaQueryWrapper<CommentLike>()
                        .eq(CommentLike::getUserId, userId)
                        .eq(CommentLike::getCommentId, commentId)
        );

        if (commentLike == null) {
            // 3. 第一次操作：点赞
            commentLike = new CommentLike();
            commentLike.setUserId(userId);
            commentLike.setCommentId(commentId);
            commentLike.setIsCancel(0);
            commentLikeMapper.insert(commentLike);

            // 更新评论点赞数 +1
            updateCommentLikes(commentId, 1);

            log.info("点赞成功，用户{}点赞评论{}", userId, commentId);
        } else {
            // 4. 已有记录，自动切换状态
            if (commentLike.getIsCancel() == 0) {
                // 当前已点赞 -> 取消点赞
                commentLike.setIsCancel(1);
                commentLikeMapper.updateById(commentLike);

                // 更新评论点赞数 -1
                updateCommentLikes(commentId, -1);

                log.info("取消点赞成功，用户{}取消点赞评论{}", userId, commentId);
            } else {
                // 当前已取消 -> 重新点赞
                commentLike.setIsCancel(0);
                commentLikeMapper.updateById(commentLike);

                // 更新评论点赞数 +1
                updateCommentLikes(commentId, 1);

                log.info("重新点赞成功，用户{}点赞评论{}", userId, commentId);
            }
        }
    }

    /**
     * 更新评论点赞数
     * @param commentId 评论ID
     * @param delta 变化量（+1或-1）
     */
    private void updateCommentLikes(Long commentId, int delta) {
        commentMapper.update(null,
                new LambdaUpdateWrapper<Comment>()
                        .eq(Comment::getId, commentId)
                        .setSql("likes = GREATEST(likes + " + delta + ", 0)")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId, Long userId) {
        // 1. 查询评论是否存在
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }

        // 2. 校验评论是否已删除
        if (comment.getIsDeleted() == 1) {
            throw new BusinessException("评论已被删除");
        }

        // 3. 校验权限：只能删除自己的评论
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException("无权删除他人评论");
        }

        // 4. 校验文章是否存在
        Article article = articleMapper.selectById(comment.getArticleId());
        if (article == null) {
            throw new BusinessException("关联文章不存在");
        }

        int deletedCount = 0;  // 删除的评论总数（包含子评论）

        // 5. 判断是一级评论还是子评论
        if (comment.getRootId() == 0) {
            // 5.1 一级评论：逻辑删除自己 + 级联删除所有子评论
            deletedCount = deleteParentComment(comment);
        } else {
            // 5.2 子评论：只逻辑删除自己
            deletedCount = deleteChildComment(comment);
        }

        // 6. 更新文章评论数
        articleMapper.update(null,
                new LambdaUpdateWrapper<Article>()
                        .eq(Article::getId, comment.getArticleId())
                        .setSql("comments = GREATEST(comments - " + deletedCount + ", 0)")
        );

        log.info("评论删除成功，评论ID：{}，用户ID：{}，删除数量：{}", commentId, userId, deletedCount);
    }

    /**
     * 删除一级评论（级联删除所有子评论）
     * @param parentComment 一级评论
     * @return 删除的评论总数
     */
    private int deleteParentComment(Comment parentComment) {
        int count = 1; // 一级评论自己

        // 1. 查询所有未删除的子评论
        List<Comment> childComments = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getRootId, parentComment.getId())
                        .eq(Comment::getIsDeleted, 0)
        );

        // 2. 批量逻辑删除子评论
        if (childComments != null && !childComments.isEmpty()) {
            List<Long> childIds = childComments.stream()
                    .map(Comment::getId)
                    .collect(Collectors.toList());

            commentMapper.update(null,
                    new LambdaUpdateWrapper<Comment>()
                            .in(Comment::getId, childIds)
                            .set(Comment::getIsDeleted, 1)
            );

            count += childComments.size();
        }

        // 3. 逻辑删除一级评论
        parentComment.setIsDeleted(1);
        commentMapper.updateById(parentComment);

        return count;
    }

    /**
     * 删除子评论（只删除自己）
     * @param childComment 子评论
     * @return 删除的评论总数
     */
    private int deleteChildComment(Comment childComment) {
        // 1. 逻辑删除子评论
        childComment.setIsDeleted(1);
        commentMapper.updateById(childComment);

        // 2. 更新父评论的子评论数
        Long rootId = childComment.getRootId();
        commentMapper.update(null,
                new LambdaUpdateWrapper<Comment>()
                        .eq(Comment::getId, rootId)
                        .setSql("sub_comment_count = GREATEST(sub_comment_count - 1, 0)")
        );

        return 1;
    }

    @Override
    public IPage<MineCommentVO> getMyComments(Long userId, Integer pageNum, Integer pageSize) {
        // 创建分页对象
        Page<MineCommentVO> page = new Page<>(pageNum, pageSize);

        // 查询我的评论列表
        IPage<MineCommentVO> result = commentMapper.selectMyComments(page, userId);

        return result;
    }
}
