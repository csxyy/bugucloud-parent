package com.bugucloud.api.web.controller.user;

import com.bugucloud.common.result.Result;
import com.bugucloud.core.vo.DashboardVO;
import com.bugucloud.core.vo.UserDetailVO;
import com.bugucloud.core.vo.UserInfoVO;
import com.bugucloud.core.vo.UserSettingVO;
import com.bugucloud.service.req.ChangePasswordReq;
import com.bugucloud.service.req.UserUpdateReq;
import com.bugucloud.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "查询当前用户基本信息")
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo() {
        Long userId = 1001L;
        UserInfoVO userInfo = userService.getUserInfo(userId);
        return Result.ok(userInfo);
    }

    @Operation(summary = "查询用户个人主页信息")
    @GetMapping("/detail/{userId}")
    public Result<UserDetailVO> getUserDetail(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        Long currentUserId = 1001L;
        UserDetailVO userDetail = userService.getUserDetail(userId, currentUserId);
        return Result.ok(userDetail);
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
