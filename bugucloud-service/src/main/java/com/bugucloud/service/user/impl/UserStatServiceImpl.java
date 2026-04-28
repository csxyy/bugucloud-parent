package com.bugucloud.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.core.entity.UserStat;
import com.bugucloud.core.mapper.UserStatMapper;
import com.bugucloud.service.user.UserStatService;
import org.springframework.stereotype.Service;

/**
 * 功能描述: 用户统计信息Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Service
public class UserStatServiceImpl extends ServiceImpl<UserStatMapper, UserStat> implements UserStatService {

}
