package com.bugucloud.service.auth.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.common.util.RedisUtil;
import com.bugucloud.core.entity.User;
import com.bugucloud.core.entity.UserStat;
import com.bugucloud.core.mapper.UserMapper;
import com.bugucloud.core.mapper.UserStatMapper;
import com.bugucloud.core.vo.LoginVO;
import com.bugucloud.core.vo.TokenRefreshVO;
import com.bugucloud.core.vo.UserInfoVO;
import com.bugucloud.service.auth.AuthService;
import com.bugucloud.service.auth.TokenService;
import com.bugucloud.service.mail.EmailService;
import com.bugucloud.service.req.LoginReq;
import com.bugucloud.service.req.RegisterReq;
import com.bugucloud.service.req.ResetPasswordReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    private final RedisUtil redisUtil;

    private final EmailService emailService;

    private final UserStatMapper userStatMapper;

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
        TokenService.TokenPair tokenPair = tokenService.createInitialTokens(user.getId());

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

    @Override
    @Async("emailExecutor")
    public void sendVerificationCode(String email) {
        log.info("异步发送验证码开始，邮箱：{}", email);

        try {
            // 1. 生成6位随机验证码
            String code = generateVerificationCode();

            // 2. 保存验证码到Redis（有效期5分钟）
            String redisKey = "verification_code:" + email;
            redisUtil.set(redisKey, code, 5, TimeUnit.MINUTES);

            // 3. 发送邮件（根据实际邮件服务实现）
            emailService.sendVerificationCode(email, code);

            log.info("验证码发送成功，邮箱：{}，验证码：{}", email, code);
        } catch (Exception e) {
            log.error("验证码发送失败，邮箱：{}", email, e);
            throw new BusinessException("验证码发送失败，请稍后重试");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterReq req) {
        // 1. 校验两次密码是否一致
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new BusinessException("两次密码输入不一致");
        }

        // 2. 校验验证码
        verifyCode(req.getEmail(), req.getCode());

        // 3. 检查邮箱是否已注册
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>()
                        .eq(User::getEmail, req.getEmail())
        );
        if (count > 0) {
            throw new BusinessException("该邮箱已被注册");
        }

        // 4. 构建用户实体
        User user = new User();
        user.setUsername(UUID.randomUUID().toString().replace("-", "")); // 占时用UUID
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEmail(req.getEmail());
        user.setNickname("用户" + generateRandomNickname());  // 生成默认昵称
        user.setAvatar(null);  // 默认头像
        user.setRole(0);  // 普通用户
        user.setStatus(1);  // 正常状态
        user.setEmailNotify(1);  // 默认开启邮件通知

        // 5. 保存用户
        userMapper.insert(user);

        // 6. 初始化用户统计信息
        initUserStat(user.getId());

        // 7. 删除验证码
        String redisKey = "verification_code:" + req.getEmail();
        redisUtil.delete(redisKey);

        log.info("用户注册成功，用户ID：{}，邮箱：{}", user.getId(), req.getEmail());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(ResetPasswordReq req) {
        // 1. 校验两次密码是否一致
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new BusinessException("两次密码输入不一致");
        }

        // 2. 校验验证码
        verifyCode(req.getEmail(), req.getCode());

        // 3. 查询用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getEmail, req.getEmail())
        );
        if (user == null) {
            throw new BusinessException("该邮箱未注册");
        }

        // 4. 更新密码
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userMapper.updateById(user);

        // 5. 删除验证码
        String redisKey = "verification_code:" + req.getEmail();
        redisUtil.delete(redisKey);

        log.info("密码重置成功，用户ID：{}，邮箱：{}", user.getId(), req.getEmail());
    }

    /**
     * 生成6位随机验证码
     */
    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    /**
     * 生成随机昵称后缀
     */
    private String generateRandomNickname() {
        return String.format("%04d", new Random().nextInt(10000));
    }

    /**
     * 校验验证码
     */
    private void verifyCode(String email, String code) {
        String redisKey = "verification_code:" + email;
        String savedCode = (String) redisUtil.get(redisKey);

        if (savedCode == null) {
            throw new BusinessException("验证码已过期，请重新获取");
        }

        if (!savedCode.equals(code)) {
            throw new BusinessException("验证码错误");
        }
    }

    /**
     * 初始化用户统计信息
     */
    private void initUserStat(Long userId) {
        UserStat userStat = new UserStat();
        userStat.setUserId(userId);
        userStat.setTotalViews(0L);
        userStat.setTotalArticles(0);
        userStat.setTotalLikes(0);
        userStat.setTotalCollects(0);
        userStat.setTotalComments(0);
        userStat.setTotalShares(0);
        userStat.setFollowerCount(0);
        userStat.setFollowCount(0);
        userStatMapper.insert(userStat);
    }
}
