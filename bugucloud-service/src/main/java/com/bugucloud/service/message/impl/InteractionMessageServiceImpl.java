package com.bugucloud.service.message.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.core.entity.InteractionMessage;
import com.bugucloud.core.entity.UserMessage;
import com.bugucloud.core.mapper.InteractionMessageMapper;
import com.bugucloud.core.mapper.UserMessageMapper;
import com.bugucloud.core.vo.UnreadCountVO;
import com.bugucloud.service.message.InteractionMessageService;
import com.bugucloud.service.message.UserMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/11 - 22:59
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InteractionMessageServiceImpl extends ServiceImpl<InteractionMessageMapper, InteractionMessage> implements InteractionMessageService {

    private final InteractionMessageMapper interactionMessageMapper;

    @Override
    public Integer getUnreadCounts(Long userId) {
        // 统计当前用户所有未读消息数量
        Long count = interactionMessageMapper.selectCount(
                new LambdaQueryWrapper<InteractionMessage>()
                        .eq(InteractionMessage::getToUserId, userId)
                        .eq(InteractionMessage::getIsRead, 0)  // 未读
        );

        return count.intValue();
    }

    @Override
    public UnreadCountVO getUnreadCountByType(Long userId, Integer msgType) {
        // 统计指定类型的未读消息数量
        Long count = interactionMessageMapper.selectCount(
                new LambdaQueryWrapper<InteractionMessage>()
                        .eq(InteractionMessage::getToUserId, userId)
                        .eq(InteractionMessage::getMsgType, msgType)
                        .eq(InteractionMessage::getIsRead, 0)  // 未读
        );

        // 构建返回VO
        UnreadCountVO vo = new UnreadCountVO();
        vo.setMsgType(msgType);
        vo.setUnreadCount(count);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readSingleMessage(Long userId, Long messageId) {
        // 1. 查询消息是否存在
        InteractionMessage message = interactionMessageMapper.selectById(messageId);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }

        // 2. 验证消息归属（只能操作自己的消息）
        if (!message.getToUserId().equals(userId)) {
            throw new BusinessException("无权操作此消息");
        }

        // 3. 如果已经是已读状态，无需更新
        if (message.getIsRead() == 1) {
            log.info("消息已经是已读状态，消息ID：{}", messageId);
            return;
        }

        // 4. 标记为已读
        message.setIsRead(1);
        interactionMessageMapper.updateById(message);

        log.info("单条消息已读成功，用户ID：{}，消息ID：{}", userId, messageId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchReadByType(Long userId, Integer msgType) {
        // 使用LambdaUpdateWrapper批量更新
        LambdaUpdateWrapper<InteractionMessage> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(InteractionMessage::getToUserId, userId)
                .eq(InteractionMessage::getMsgType, msgType)
                .eq(InteractionMessage::getIsRead, 0)  // 只更新未读的
                .set(InteractionMessage::getIsRead, 1); // 设置为已读

        int updateCount = interactionMessageMapper.update(null, updateWrapper);

        log.info("批量已读完成，用户ID：{}，消息类型：{}，影响行数：{}", userId, msgType, updateCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readAllMessages(Long userId) {
        // 使用LambdaUpdateWrapper批量更新所有未读消息
        LambdaUpdateWrapper<InteractionMessage> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(InteractionMessage::getToUserId, userId)
                .eq(InteractionMessage::getIsRead, 0)  // 只更新未读的
                .set(InteractionMessage::getIsRead, 1); // 设置为已读

        int updateCount = interactionMessageMapper.update(null, updateWrapper);

        log.info("全部消息已读完成，用户ID：{}，影响行数：{}", userId, updateCount);
    }

}
