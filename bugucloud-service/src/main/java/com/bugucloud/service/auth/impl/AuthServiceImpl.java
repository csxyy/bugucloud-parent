package com.bugucloud.service.auth.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.core.entity.User;
import com.bugucloud.core.mapper.UserMapper;
import com.bugucloud.core.vo.LoginVO;
import com.bugucloud.core.vo.TokenRefreshVO;
import com.bugucloud.core.vo.UserInfoVO;
import com.bugucloud.service.auth.AuthService;
import com.bugucloud.service.auth.TokenService;
import com.bugucloud.service.req.LoginReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/21 - 22:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    @Override
    public LoginVO login(LoginReq req) {
        // 1. 判断输入是用户名还是邮箱（包含@符号认为是邮箱）
        boolean isEmail = req.getUsername().contains("@");

        // 2. 查询用户
        User user;
        if (isEmail) {
            user = userMapper.selectOne(
                    new LambdaQueryWrapper<User>()
                            .eq(User::getEmail, req.getUsername())
            );
        } else {
            user = userMapper.selectOne(
                    new LambdaQueryWrapper<User>()
                            .eq(User::getUsername, req.getUsername())
            );
        }

        // 3. 校验用户是否存在
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 4. 校验用户状态
        if (user.getStatus() != 1) {
            if (user.getStatus() == 0) {
                throw new BusinessException("账号已被禁用，请联系管理员");
            } else if (user.getStatus() == -1) {
                throw new BusinessException("账号已注销");
            }
        }

        // 5. 校验密码（使用BCrypt加密）
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 6. 生成Token
        TokenService.TokenPair tokenPair = tokenService.createInitialTokens(req.getUsername());

        // 7. 更新最后登录信息
        user.setLastLoginTime(LocalDateTime.now());
        // user.setLastLoginIp(ipAddress); // 如果有IP地址
        userMapper.updateById(user);

        // 8. 构建用户信息
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserId(user.getId());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setEmail(user.getEmail());
        userInfo.setRole(user.getRole());

        // 9. 构建登录响应
        return LoginVO.builder()
                .accessToken(tokenPair.accessToken())
                .refreshToken(tokenPair.refreshToken())
                .userInfo(userInfo)
                .build();
    }

    @Override
    public TokenRefreshVO refreshToken(String refreshToken) {
        // 1. 校验参数
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BusinessException("刷新令牌不能为空");
        }

        // 2. 调用TokenService刷新Token
        TokenService.TokenPair tokenPair = tokenService.refresh(refreshToken);

        // 3. 构建响应
        return TokenRefreshVO.builder()
                .accessToken(tokenPair.accessToken())
                .refreshToken(tokenPair.refreshToken())
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        // 1. 校验参数
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BusinessException("刷新令牌不能为空");
        }

        // 2. 调用TokenService登出
        tokenService.logout(refreshToken);

        log.info("用户登出成功");
    }
}
