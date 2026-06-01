package com.bugucloud.api.web.controller;

import com.bugucloud.common.result.Result;
import com.bugucloud.common.util.SecurityUtil;
import com.bugucloud.core.vo.*;
import com.bugucloud.service.req.ChangePasswordReq;
import com.bugucloud.service.req.UserUpdateReq;
import com.bugucloud.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能描述: 用户中心
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/27 - 23:29
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户中心")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 查询用户个人主页信息（包含个人成就）
     */
    @Operation(summary = "查询用户个人主页信息")
    @GetMapping("/detail/{userId}")
    public Result<UserDetailVO> getUserDetail(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        UserDetailVO userDetail = userService.getUserDetail(userId, currentUserId);
        return Result.ok(userDetail);
    }

    /**
     * 查询用户的文章列表
     */
    @Operation(summary = "查询用户的文章列表")
    @GetMapping("/articles/{userId}")
    public Result<List<UserArticleVO>> getUserArticles(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<UserArticleVO> articles = userService.getUserArticles(userId);
        return Result.ok(articles);
    }

    // ===================================== 个人中心 ========================================

    @Operation(summary = "查询控制台信息")
    @GetMapping("/dashboard")
    public Result<DashboardVO> getDashboard() {
        Long userId = 1001L;
        DashboardVO dashboard = userService.getDashboard(userId);
        return Result.ok(dashboard);
    }

    @Operation(summary = "查询用户设置信息")
    @GetMapping("/settings")
    public Result<UserSettingVO> getUserSettings() {
        Long userId = 1001L;
        UserSettingVO userSettings = userService.getUserSettings(userId);
        return Result.ok(userSettings);
    }

    @Operation(summary = "更新用户设置信息")
    @PutMapping("/settings")
    public Result<Void> updateUserSettings(@Valid @RequestBody UserUpdateReq req) {
        Long userId = 1001L;
        userService.updateUserSettings(userId, req);
        return Result.ok();
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordReq req) {

        return Result.ok();
    }
}
