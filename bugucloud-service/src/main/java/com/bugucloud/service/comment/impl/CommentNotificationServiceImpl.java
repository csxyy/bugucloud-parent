package com.bugucloud.service.comment.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bugucloud.core.entity.*;
import com.bugucloud.core.mapper.InteractionMessageMapper;
import com.bugucloud.service.comment.CommentNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/7 - 11:30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentNotificationServiceImpl implements CommentNotificationService {

    private final InteractionMessageMapper interactionMessageMapper;

    /**
     * 发送评论通知（重构后，只操作t_interaction_msg_unread表）
     *
     * @param comment 当前评论
     * @param article 文章信息
     * @param fromUserId 评论者ID
     * @param parentId 父评论ID（0=一级评论）
     */
    @Override
    @Async("commentNotificationExecutor")
    public void sendCommentNotifications(Comment comment, Article article, Long fromUserId, Long parentId) {
        log.info("异步发送评论通知开始，评论ID：{}，评论者：{}", comment.getId(), fromUserId);

        try {
            List<Long> targetUserIds = calculateTargetUsers(comment, article, fromUserId, parentId);

            if (CollectionUtils.isEmpty(targetUserIds)) {
                log.info("无需发送评论通知，评论ID：{}", comment.getId());
                return;
            }

            // 构建批量未读消息列表
            List<InteractionMessage> messages = buildBatchUnreadMessages(comment, fromUserId, targetUserIds);

            // 批量插入未读消息
            for (InteractionMessage message : messages) {
                try {
                    interactionMessageMapper.insert(message);
                } catch (DuplicateKeyException e) {
                    // 如果违反唯一约束，说明已存在相同消息，跳过
                    log.warn("消息已存在，跳过插入，toUserId：{}，msgType：{}，businessId：{}",
                            message.getToUserId(), message.getMsgType(), message.getBusinessId());
                }
            }

            log.info("异步发送评论通知完成，评论ID：{}，通知人数：{}",
                    comment.getId(), targetUserIds.size());
        } catch (Exception e) {
            log.error("异步发送评论通知失败，评论ID：{}", comment.getId(), e);
        }
    }

    /**
     * 计算需要通知的目标用户
     * 业务规则：
     * 1. 一级评论：只通知文章作者
     * 2. 二级评论：只通知被回复的用户
     */
    private List<Long> calculateTargetUsers(Comment comment, Article article, Long fromUserId, Long parentId) {
        List<Long> targetUserIds = new ArrayList<>();

        if (parentId == 0L) {
            // 一级评论：通知文章作者（排除自己评论自己的文章）
            if (!article.getUserId().equals(fromUserId)) {
                targetUserIds.add(article.getUserId());
            }
        } else {
            // 二级评论：通知被回复的用户（排除自己回复自己）
            Long replyUserId = comment.getReplyUserId();
            if (replyUserId != null && !replyUserId.equals(fromUserId)) {
                targetUserIds.add(replyUserId);
            }
        }

        return targetUserIds;
    }

    /**
     * 构建批量未读消息列表
     */
    private List<InteractionMessage> buildBatchUnreadMessages(Comment comment, Long fromUserId, List<Long> targetUserIds) {
        // 消息类型：3=评论（一级评论和二级评论都属于评论类型）
        // 业务ID：使用评论ID作为业务主键
        return targetUserIds.stream()
                .map(targetUserId -> {
                    InteractionMessage message = new InteractionMessage();
                    message.setToUserId(targetUserId);
                    message.setFromUserId(fromUserId);
                    message.setMsgType(3);  // 3=评论
                    message.setBusinessId(comment.getId());  // 评论ID作为业务主键
                    message.setIsRead(0);  // 未读
                    return message;
                })
                .toList();
    }
}
