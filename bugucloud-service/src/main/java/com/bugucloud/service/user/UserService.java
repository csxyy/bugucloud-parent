package com.bugucloud.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bugucloud.core.entity.User;
import com.bugucloud.core.vo.*;
import com.bugucloud.service.req.ChangePasswordReq;
import com.bugucloud.service.req.UserUpdateReq;

import java.util.List;

/**
 * 功能描述: 用户信息Service
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
public interface UserService extends IService<User> {

    /**
     * 查询用户个人主页信息（包含个人成就）
     * @param userId 目标用户ID
     * @param currentUserId 当前登录用户ID（可为null）
     * @return 用户详情
     */
    UserDetailVO getUserDetail(Long userId, Long currentUserId);

    /**
     * 查询用户的文章列表
     * @param userId 用户ID
     * @return 文章列表
     */
    List<UserArticleVO> getUserArticles(Long userId);


    // ===================================== 个人中心 ========================================

    /**
     * 查询控制台信息
     * @param userId 用户ID
     * @return 控制台信息
     */
    DashboardVO getDashboard(Long userId);

    /**
     * 查询用户信息设置
     * @param userId 当前用户ID
     * @return 用户设置信息
     */
    UserSettingVO getUserSettings(Long userId);

    /**
     * 更新用户设置信息
     * @param userId 当前用户ID
     * @param req 更新请求
     */
    void updateUserSettings(Long userId, UserUpdateReq req);


    /**
     * 修改密码
     * @param userId 当前用户ID
     * @param req 修改密码请求
     */
    void changePassword(Long userId, ChangePasswordReq req);
}
