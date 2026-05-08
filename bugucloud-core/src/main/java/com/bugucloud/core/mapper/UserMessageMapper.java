package com.bugucloud.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugucloud.core.entity.UserMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能描述: 消息通知Mapper
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:39
 */
public interface UserMessageMapper extends BaseMapper<UserMessage> {

    /**
     * 批量插入消息通知
     */
    int batchInsertMessages(@Param("messages") List<UserMessage> messages);

}
