package com.bugucloud.service.message.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.core.entity.UserMessage;
import com.bugucloud.core.mapper.UserMessageMapper;
import com.bugucloud.service.message.UserMessageService;
import org.springframework.stereotype.Service;

/**
 * 功能描述: 消息通知Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

}
