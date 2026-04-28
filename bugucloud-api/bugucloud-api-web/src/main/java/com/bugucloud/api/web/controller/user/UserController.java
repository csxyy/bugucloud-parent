package com.bugucloud.api.web.controller.user;

import com.bugucloud.api.web.controller.user.req.ChangePasswordReq;
import com.bugucloud.api.web.controller.user.req.UserUpdateReq;
import com.bugucloud.api.web.controller.user.vo.DashboardVO;
import com.bugucloud.api.web.controller.user.vo.UserInfoVO;
import com.bugucloud.api.web.controller.user.vo.UserProfileVO;
import com.bugucloud.api.web.controller.user.vo.UserSettingVO;
import com.bugucloud.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class UserController {

    @Operation(summary = "查询用户基本信息")
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo() {
        return Result.ok();
    }

    @Operation(summary = "查询用户个人主页信息")
    @GetMapping("/{userId}")
    public Result<UserProfileVO> getUserProfile(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.ok();
    }

    // ===================================== 个人中心 ========================================

    @Operation(summary = "查询控制台信息")
    @GetMapping("/dashboard")
    public Result<DashboardVO> getDashboard() {
        return Result.ok();
    }

    @Operation(summary = "上传图片到OSS")
    @PostMapping("/upload-image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        return Result.ok();
    }

    @Operation(summary = "查询用户信息设置")
    @GetMapping("/settings")
    public Result<UserSettingVO> getUserSettings() {
        return Result.ok();
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/settings")
    public Result<Void> updateUserSettings(@Valid @RequestBody UserUpdateReq request) {

        return Result.ok();
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordReq request) {

        return Result.ok();
    }
}
