package com.bugucloud.api.web.controller.follow;

import com.bugucloud.common.result.Result;
import com.bugucloud.core.vo.FollowListVO;
import com.bugucloud.core.vo.LikeCollectMessageVO;
import com.bugucloud.service.article.ArticleLikeCollectService;
import com.bugucloud.service.follow.UserFollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/follow")
@Tag(name = "关注管理")
@RequiredArgsConstructor
public class FollowController {

    private final UserFollowService userFollowService;
    private final ArticleLikeCollectService articleLikeCollectService;

    @Operation(summary = "查询我的关注列表")
    @GetMapping("/following")
    public Result<List<FollowListVO>> listMyFollowing() {
        Long userId = 1003L;
        List<FollowListVO> followingList = userFollowService.listMyFollowing(userId);
        return Result.ok(followingList);
    }

    @Operation(summary = "查询我的粉丝列表")
    @GetMapping("/followers")
    public Result<List<FollowListVO>> listMyFollowers() {
        Long userId = 1005L;
        List<FollowListVO> followersList = userFollowService.listMyFollowers(userId);
        return Result.ok(followersList);
    }

    @Operation(summary = "查询我的获赞和收藏消息")
    @GetMapping("/like-collect-messages")
    public Result<List<LikeCollectMessageVO>> listLikeCollectMessages() {
        Long userId = 1001L;
        List<LikeCollectMessageVO> messages = articleLikeCollectService.listLikeCollectMessages(userId);
        return Result.ok(messages);
    }

    @Operation(summary = "关注/取消关注用户")
    @PutMapping("/{targetUserId}")
    public Result<Boolean> toggleFollow(@Parameter(description = "被关注的用户id")
                                     @PathVariable Long targetUserId,
                                     @Parameter(description = "关注来源 1=博客 2=主页 3=粉丝列表")
                                     @RequestParam Integer source) {
        Long currentUserId = 1001L;
        Boolean isFollowed  = userFollowService.toggleFollow(currentUserId, targetUserId, source);
        return Result.ok(isFollowed);
    }

    @Operation(summary = "求更新（1天1次）")
    @PutMapping("/request-update/{targetUserId}")
    public Result<Void> requestUpdate(@Parameter(description = "被求更新的用户id")
                                      @PathVariable Long targetUserId) {
        Long currentUserId = 1001L;
        userFollowService.requestUpdate(currentUserId, targetUserId);
        return Result.ok();
    }

}
