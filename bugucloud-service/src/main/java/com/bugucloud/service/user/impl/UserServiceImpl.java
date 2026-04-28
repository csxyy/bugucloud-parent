package com.bugucloud.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.core.entity.User;
import com.bugucloud.core.mapper.UserMapper;
import com.bugucloud.service.user.UserService;
import org.springframework.stereotype.Service;

/**
 * 功能描述: 用户信息Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
