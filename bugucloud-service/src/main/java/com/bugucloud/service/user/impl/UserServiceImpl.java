package com.bugucloud.service.user.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.core.entity.User;
import com.bugucloud.core.mapper.UserMapper;
import com.bugucloud.core.vo.*;
import com.bugucloud.service.req.UserUpdateReq;
import com.bugucloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

/**
 * 功能描述: 用户信息Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserDetailVO getUserDetail(Long userId, Long currentUserId) {
        UserDetailVO userDetail = userMapper.selectUserDetail(userId, currentUserId);

        if (userDetail == null) {
            throw new BusinessException("用户不存在或已被禁用");
        }

        // 判断是否是当前用户
        boolean isSelf = currentUserId != null && currentUserId.equals(userId);
        userDetail.setIsSelf(isSelf);

        // 格式化IP属地：中国只显示省份，其他国家显示国家
        userDetail.setIpLocation(formatIpLocation(userDetail.getIpLocation()));

        return userDetail;
    }

    /**
     * 格式化IP属地
     * 规则：
     * - 中国的只显示省份（如：中国 上海 → 上海）
     * - 其他国家的显示国家名（如：美国 → 美国）
     * - 没有IP属地的显示"未知"
     */
    private String formatIpLocation(String ipLocation) {
        if (ipLocation == null || ipLocation.isBlank()) {
            return "未知";
        }

        // 去除首尾空格
        String location = ipLocation.trim();

        // 如果包含"中国"，只显示后面的省份
        if (location.startsWith("中国")) {
            // 去掉"中国"和空格
            String province = location.replace("中国", "").trim();
            // 如果省份为空，返回"未知"
            return province.isEmpty() ? "未知" : province;
        }

        // 其他国家直接返回
        return location;
    }

    @Override
    public List<UserArticleVO> getUserArticles(Long userId) {
        List<UserArticleVO> articles = userMapper.selectUserArticles(userId);
        return articles != null ? articles : Collections.emptyList();
    }


    @Override
    public DashboardVO getDashboard(Long userId) {
        // 1. 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null || user.getStatus() != 1) {
            throw new BusinessException("用户不存在或已被禁用");
        }

        // 2. 构建DashboardVO
        DashboardVO dashboard = new DashboardVO();
        dashboard.setRole(user.getRole());
        dashboard.setVipStartTime(user.getVipStartTime());
        dashboard.setVipExpireTime(user.getVipExpireTime());

        // 3. 计算会员剩余天数
        dashboard.setVipRemainingDays(calculateVipRemainingDays(user));

        // 4. 查询消息通知是否开启（查询email_notify状态）
        dashboard.setNotificationEnabled(checkNotificationEnabled(user.getEmailNotify()));

        return dashboard;
    }

    /**
     * 计算会员剩余天数
     */
    private Long calculateVipRemainingDays(User user) {
        // 普通用户没有会员
        if (user.getRole() == null || user.getRole() == 0) {
            return 0L;
        }

        // 管理员永久有效
        if (user.getRole() == 3) {
            return -1L; // -1表示永久有效
        }

        // 检查会员过期时间
        if (user.getVipExpireTime() == null) {
            return 0L;
        }

        LocalDateTime now = LocalDateTime.now();

        // 如果已过期，返回0
        if (now.isAfter(user.getVipExpireTime())) {
            // 可以在这里更新用户角色为普通用户
            // user.setRole(0);
            // userMapper.updateById(user);
            return 0L;
        }

        // 计算剩余天数
        return ChronoUnit.DAYS.between(now, user.getVipExpireTime());
    }

    /**
     * 检查消息通知是否开启
     */
    private Boolean checkNotificationEnabled(Integer emailNotify) {
        // 如果有消息记录，返回最新一条的邮件通知状态；否则默认开启
        return emailNotify == null || (emailNotify.equals(1));
    }


    @Override
    public UserSettingVO getUserSettings(Long userId) {
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null || user.getStatus() != 1) {
            throw new BusinessException("用户不存在或已被禁用");
        }

        // 转换为VO
        UserSettingVO settingVO = new UserSettingVO();
        settingVO.setUserId(user.getId());
        settingVO.setAvatar(user.getAvatar());
        settingVO.setNickname(user.getNickname());
        settingVO.setEmail(user.getEmail());
        settingVO.setPersonalIntro(user.getPersonalIntro());

        return settingVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserSettings(Long userId, UserUpdateReq req) {
        // 1. 查询用户是否存在且正常
        User user = userMapper.selectById(userId);
        if (user == null || user.getStatus() != 1) {
            throw new BusinessException("用户不存在或已被禁用");
        }

        // 2. 判断是否有需要更新的字段
        boolean hasUpdate = false;

        // 3. 更新头像
        if (StrUtil.isNotBlank(req.getAvatar()) && !req.getAvatar().equals(user.getAvatar())) {
            user.setAvatar(req.getAvatar());
            hasUpdate = true;
        }

        // 4. 更新昵称
        if (StrUtil.isNotBlank(req.getNickname())&& !req.getNickname().equals(user.getNickname())) {
            user.setNickname(req.getNickname());
            hasUpdate = true;
        }

        // 5. 更新个人简介
        if (StrUtil.isNotBlank(req.getPersonalIntro()) && !req.getPersonalIntro().equals(user.getPersonalIntro())) {
            user.setPersonalIntro(req.getPersonalIntro());
            hasUpdate = true;
        }

        // 6. 如果有更新才执行数据库操作
        if (hasUpdate) {
            userMapper.updateById(user);
            log.info("用户设置信息更新成功，用户ID：{}", userId);
        } else {
            log.info("用户设置信息无变化，用户ID：{}", userId);
        }
    }
}
