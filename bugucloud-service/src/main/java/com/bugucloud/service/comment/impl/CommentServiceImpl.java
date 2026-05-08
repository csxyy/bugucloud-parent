package com.bugucloud.service.comment.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.core.entity.*;
import com.bugucloud.core.mapper.*;
import com.bugucloud.core.vo.CommentVO;
import com.bugucloud.core.vo.MyCommentVO;
import com.bugucloud.core.vo.SubCommentVO;
import com.bugucloud.service.comment.CommentNotificationService;
import com.bugucloud.service.comment.CommentService;
import com.bugucloud.service.req.CommentCreateReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

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
    public List<CommentVO> getCommentList(Long articleId, Long currentUserId) {
        // 查询一级评论（包含子评论）
        List<CommentVO> commentList = commentMapper.selectParentComments(articleId, currentUserId);

        // 如果用户未登录，将所有isLiked设置为false
        if (currentUserId == null) {
            setDefaultLikedStatus(commentList);
        }

        return commentList != null ? commentList : Collections.emptyList();
    }

    /**
     * 未登录用户，将所有点赞状态设置为false
     */
    private void setDefaultLikedStatus(List<CommentVO> commentList) {
        if (commentList == null) {
            return;
        }

        for (CommentVO comment : commentList) {
            comment.setIsLiked(false);

            if (comment.getChildren() != null) {
                for (SubCommentVO child : comment.getChildren()) {
                    child.setIsLiked(false);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createComment(CommentCreateReq req, Long userId) {
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

        // 3. 构建主评论实体
        Comment comment = new Comment();
        comment.setArticleId(req.getArticleId());
        comment.setUserId(userId);
        comment.setNickname(user.getNickname());
        comment.setAvatar(user.getAvatar());
        comment.setContent(req.getContent());
        comment.setLikes(0);
        comment.setIsDeleted(0);

        // 4. 处理父评论ID（0表示一级评论）
        Long parentId = req.getParentId() != null ? req.getParentId() : 0L;
        comment.setParentId(parentId);

        // 5. 处理回复逻辑
        if (parentId != 0L) {
            // 查询父评论信息
            Comment parentComment = commentMapper.selectById(parentId);
            if (parentComment == null || parentComment.getIsDeleted() == 1) {
                throw new BusinessException("父评论不存在或已删除");
            }

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

        // 6. 插入评论
        int insertCount = commentMapper.insertComment(comment);
        if (insertCount <= 0) {
            throw new BusinessException("评论创建失败");
        }

        // 7. 更新文章评论数
        articleMapper.update(null,
                new LambdaUpdateWrapper<Article>()
                        .eq(Article::getId, req.getArticleId())
                        .setSql("comments = comments + 1")
        );

        // 8. 发送消息通知
        commentNotificationService.sendCommentNotifications(comment, article, userId, parentId);

        return comment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean likeComment(Long commentId, Long userId, Boolean isLike) {
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
            // 3. 第一次操作
            if (isLike) {
                // 3.1 第一次点赞：创建记录
                commentLike = new CommentLike();
                commentLike.setUserId(userId);
                commentLike.setCommentId(commentId);
                commentLike.setIsCancel(0);
                commentLikeMapper.insert(commentLike);

                // 更新评论点赞数 +1
                updateCommentLikes(commentId, 1);

                return true;
            } else {
                // 3.2 第一次就取消点赞：无需操作
                return false;
            }
        } else {
            // 4. 已有记录，根据操作类型和当前状态处理
            if (isLike) {
                // 4.1 点赞操作
                if (commentLike.getIsCancel() == 0) {
                    // 已经点赞，无需操作
                    return true;
                } else {
                    // 之前取消过，重新点赞：is_cancel = 0
                    commentLike.setIsCancel(0);
                    commentLikeMapper.updateById(commentLike);

                    // 更新评论点赞数 +1
                    updateCommentLikes(commentId, 1);

                    return true;
                }
            } else {
                // 4.2 取消点赞操作
                if (commentLike.getIsCancel() == 1) {
                    // 已经取消，无需操作
                    return false;
                } else {
                    // 取消点赞：is_cancel = 1
                    commentLike.setIsCancel(1);
                    commentLikeMapper.updateById(commentLike);

                    // 更新评论点赞数 -1
                    updateCommentLikes(commentId, -1);

                    return false;
                }
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
                        .setSql("likes = likes + " + delta)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteComment(Long commentId, Long userId) {
        // 1. 查询评论是否存在
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() == 1) {
            throw new BusinessException("评论不存在或已删除");
        }

        // 2. 权限校验：只能删除自己的评论
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException("无权删除他人评论");
        }

        // 3. 获取文章信息（用于后续更新评论数）
        Article article = articleMapper.selectById(comment.getArticleId());
        if (article == null) {
            throw new BusinessException("关联文章不存在");
        }

        // 4. 逻辑删除评论
        comment.setIsDeleted(1);
        int updateCount = commentMapper.updateById(comment);
        if (updateCount <= 0) {
            throw new BusinessException("删除评论失败");
        }

        // 5. 更新文章评论数 -1
        // 如果是一级评论，直接减1；如果是子评论，也减1（文章总评论数包含所有评论）
        articleMapper.update(null,
                new LambdaUpdateWrapper<Article>()
                        .eq(Article::getId, comment.getArticleId())
                        .setSql("comments = GREATEST(comments - 1, 0)")  // 防止负数
        );

        log.info("评论删除成功，评论ID：{}，用户ID：{}，文章ID：{}", commentId, userId, comment.getArticleId());

        return true;
    }

    @Override
    public IPage<MyCommentVO> getMyComments(Integer pageNum, Integer pageSize,
                                            Integer queryType, Long userId) {
        // 创建分页对象
        Page<MyCommentVO> page = new Page<>(pageNum, pageSize);

        // 查询我的评论列表
        IPage<MyCommentVO> result = commentMapper.selectMyComments(page, userId, queryType);

        return result;
    }
}
