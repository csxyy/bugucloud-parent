package com.bugucloud.service.comment.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bugucloud.core.entity.*;
import com.bugucloud.core.mapper.CommentMapper;
import com.bugucloud.core.mapper.UserMessageMapper;
import com.bugucloud.service.comment.CommentNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private final UserMessageMapper userMessageMapper;

    /**
     * 发送评论通知（根据业务规则）
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

            // 构建批量消息列表
            List<UserMessage> messages = buildBatchMessages(comment, article, fromUserId, targetUserIds);

            // 批量插入
            int insertCount = userMessageMapper.batchInsertMessages(messages);

            log.info("异步发送评论通知完成，评论ID：{}，通知人数：{}，插入条数：{}",
                    comment.getId(), targetUserIds.size(), insertCount);
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
     * 构建批量消息列表
     */
    private List<UserMessage> buildBatchMessages(Comment comment, Article article,
                                                 Long fromUserId, List<Long> targetUserIds) {
        Integer msgType = comment.getParentId() == 0L ? 2 : 3; // 2=评论 3=回复

        // 根据消息类型拼接内容
        String content = msgType == 2 ?
                "评论了你的文章《%s》".formatted(article.getTitle()) :
                "回复了你的评论：%s".formatted(comment.getContent());

        return targetUserIds.stream()
                .map(targetUserId -> {
                    UserMessage message = new UserMessage();
                    message.setToUserId(targetUserId);
                    message.setFromUserId(fromUserId);
                    message.setFromNickname(comment.getNickname());
                    message.setFromAvatar(comment.getAvatar());
                    message.setMsgType(msgType);
                    message.setArticleId(article.getId());
                    message.setCommentId(comment.getId());
                    message.setContent(content);
                    message.setIsRead(0);
                    message.setEmailNotify(0);
                    return message;
                }).toList();
    }
}
