package com.bugucloud.service.comment;

import com.bugucloud.core.entity.Article;
import com.bugucloud.core.entity.Comment;

/**
 * 功能描述: 评论通知异步服务
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/7 - 11:30
 */
public interface CommentNotificationService {

    /**
     * 异步发送评论通知
     */
    void sendCommentNotifications(Comment comment, Article article, Long fromUserId, Long parentId);
}
