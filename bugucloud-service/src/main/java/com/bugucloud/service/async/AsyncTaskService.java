package com.bugucloud.service.async;

import com.bugucloud.core.entity.ArticleLikeCollect;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/28 - 23:04
 */
public interface AsyncTaskService {
    /**
     * 更新文章统计
     */
    void updateArticleStats(Long articleId, Integer msgType, boolean isActive);

    /**
     * 更新用户统计
     */
    void updateUserStats(Long userId, Integer msgType, boolean isActive);

    /**
     * 发送点赞/收藏通知
     */
    void sendLikeCollectNotification(ArticleLikeCollect likeCollect, Long targetUserId);
}
