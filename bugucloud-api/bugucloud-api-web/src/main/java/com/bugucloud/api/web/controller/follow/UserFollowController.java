package com.bugucloud.api.web.controller.follow;

import com.bugucloud.api.web.controller.follow.vo.FollowListVO;
import com.bugucloud.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能描述: 用户关注接口
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 17:09
 */
@RestController
@RequestMapping("/userFollow")
@Tag(name = "用户关注管理")
public class UserFollowController {
    @Operation(summary = "查询关注列表")
    @GetMapping("/follows")
    public Result<List<FollowListVO>> listFollows() {
        return Result.ok();
    }

    @Operation(summary = "关注用户")
    @PostMapping("/follow/{userId}")
    public Result<Void> followUser(
            @Parameter(description = "被关注用户ID") @PathVariable Long userId) {

        return Result.ok();
    }

    @Operation(summary = "求更新")
    @PostMapping("/request-update/{userId}")
    public Result<Void> requestUpdate(
            @Parameter(description = "被求更新用户ID") @PathVariable Long userId) {

        return Result.ok();
    }
}
