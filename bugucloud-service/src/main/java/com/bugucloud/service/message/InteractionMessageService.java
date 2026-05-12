package com.bugucloud.service.message;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bugucloud.core.entity.InteractionMessage;
import com.bugucloud.core.vo.UnreadCountVO;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/11 - 22:59
 */
public interface InteractionMessageService extends IService<InteractionMessage> {

    /**
     * 获取用户所有类型消息的未读数量
     * @param userId 当前用户ID
     * @return 未读消息总数
     */
    Integer getUnreadCounts(Long userId);

    /**
     * 获取指定类型消息的未读数量
     * @param userId 当前用户ID
     * @param msgType 消息类型 1=获赞和收藏 2=粉丝 3=评论 4=系统通知
     * @return 未读数量统计
     */
    UnreadCountVO getUnreadCountByType(Long userId, Integer msgType);

    /**
     * 单条消息已读
     * @param userId 当前用户ID
     * @param messageId 消息ID
     */
    void readSingleMessage(Long userId, Long messageId);

    /**
     * 批量已读（按消息类型）
     * @param userId 当前用户ID
     * @param msgType 消息类型 1=获赞和收藏 2=粉丝 3=评论 4=系统通知
     * @return 影响行数
     */
    void batchReadByType(Long userId, Integer msgType);

    /**
     * 全部消息已读
     * @param userId 当前用户ID
     */
    void readAllMessages(Long userId);

}
