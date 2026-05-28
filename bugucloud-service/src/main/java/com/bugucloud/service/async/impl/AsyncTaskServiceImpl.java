package com.bugucloud.service.async.impl;

import com.bugucloud.core.entity.Article;
import com.bugucloud.core.entity.ArticleLikeCollect;
import com.bugucloud.core.entity.InteractionMessage;
import com.bugucloud.core.entity.UserStat;
import com.bugucloud.core.mapper.ArticleMapper;
import com.bugucloud.core.mapper.InteractionMessageMapper;
import com.bugucloud.core.mapper.UserStatMapper;
import com.bugucloud.service.async.AsyncTaskService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/28 - 23:04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncTaskServiceImpl implements AsyncTaskService {

    private final ArticleMapper articleMapper;
    private final UserStatMapper userStatMapper;
    private final InteractionMessageMapper interactionMessageMapper;

    @Override
    @Async("articleStatsExecutor")
    public void updateArticleStats(Long articleId, Integer msgType, boolean isActive) {
        log.info("异步更新文章统计，文章ID：{}，类型：{}，是否触发：{}", articleId, msgType, isActive);

        try {
            String field = msgType == 1 ? "likes" : "collects";
            String sql = isActive ? field + " = " + field + " + 1" : field + " = GREATEST(" + field + " - 1, 0)";

            articleMapper.update(null,
                    new LambdaUpdateWrapper<Article>()
                            .eq(Article::getId, articleId)
                            .setSql(sql)
            );

            log.info("文章统计更新成功，文章ID：{}", articleId);
        } catch (Exception e) {
            log.error("文章统计更新失败，文章ID：{}", articleId, e);
        }
    }

    @Override
    @Async("userStatsExecutor")
    public void updateUserStats(Long userId, Integer msgType, boolean isActive) {
        log.info("异步更新用户统计，用户ID：{}，类型：{}，是否触发：{}", userId, msgType, isActive);

        try {
            String field = msgType == 1 ? "total_likes" : "total_collects";
            String sql = isActive ? field + " = " + field + " + 1" : field + " = GREATEST(" + field + " - 1, 0)";

            userStatMapper.update(null,
                    new LambdaUpdateWrapper<UserStat>()
                            .eq(UserStat::getUserId, userId)
                            .setSql(sql)
            );

            log.info("用户统计更新成功，用户ID：{}", userId);
        } catch (Exception e) {
            log.error("用户统计更新失败，用户ID：{}", userId, e);
        }
    }

    @Override
    @Async("notificationExecutor")
    public void sendLikeCollectNotification(ArticleLikeCollect likeCollect, Long targetUserId) {
        log.info("异步发送点赞/收藏通知，操作人：{}，目标用户：{}", likeCollect.getUserId(), targetUserId);

        try {
            InteractionMessage message = new InteractionMessage();
            message.setToUserId(targetUserId);
            message.setFromUserId(likeCollect.getUserId());
            message.setMsgType(1);  // 1=获赞和收藏
            message.setBusinessId(likeCollect.getId());
            message.setIsRead(0);

            interactionMessageMapper.insert(message);

            log.info("点赞/收藏通知发送成功");
        } catch (DuplicateKeyException e) {
            log.warn("消息已存在，跳过插入");
        } catch (Exception e) {
            log.error("点赞/收藏通知发送失败", e);
        }
    }
}
