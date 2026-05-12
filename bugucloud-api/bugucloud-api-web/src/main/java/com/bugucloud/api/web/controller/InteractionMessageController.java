package com.bugucloud.api.web.controller;

import com.bugucloud.common.result.Result;
import com.bugucloud.core.vo.UnreadCountVO;
import com.bugucloud.service.message.InteractionMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

/**
 * 功能描述: 互动消息未读状态表（系统通知类）
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/11 - 22:51
 */
@RestController
@RequestMapping("/message")
@Tag(name = "互动消息未读状态管理")
@RequiredArgsConstructor
public class InteractionMessageController {

    private final InteractionMessageService interactionMessageService;

    /**
     * 获取所有类型消息的未读数量（小红点汇总）
     * GET /message/unread/counts?userId=1005
     */
    @Operation(summary = "获取用户所有类型消息的未读数量（小红点汇总）")
    @GetMapping("/unread/counts")
    public Result<Integer> getUnreadCounts() {
        Long userId = 1001L;
        Integer unreadCount = interactionMessageService.getUnreadCounts(userId);
        return Result.ok(unreadCount);
    }

    /**
     * 获取指定类型消息的未读数量
     * GET /message/unread/count?userId=1005&msgType=1
     */
    @Operation(summary = "获取指定类型消息的未读数量")
    @GetMapping("/unread/count")
    public Result<UnreadCountVO> getUnreadCount(
            @Parameter(description = "消息类型 1=获赞和收藏 2=粉丝 3=评论 4=系统通知")
            @RequestParam Integer msgType) {
        Long userId = 1001L;
        UnreadCountVO unreadCount = interactionMessageService.getUnreadCountByType(userId, msgType);
        return Result.ok(unreadCount);
    }

    /**
     * 单条消息已读
     * PUT /message/read/{messageId}?userId=1005
     */
    @Operation(summary = "单条消息已读")
    @PutMapping("/read/{messageId}")
    public Result<Void> readSingle(@Parameter(description = "消息ID")
                                       @PathVariable Long messageId) {
        Long userId = 1001L;
        interactionMessageService.readSingleMessage(userId, messageId);
        return Result.ok();
    }

    /**
     * 批量已读（按消息类型）
     * PUT /message/batch/read
     */
    @Operation(summary = "批量已读（按消息类型）")
    @PutMapping("/batch/read")
    public Result<Void> batchRead(
            @Parameter(description = "消息类型 1=获赞和收藏 2=粉丝 3=评论 4=系统通知")
            @RequestParam Integer msgType) {
        Long userId = 1001L;
        interactionMessageService.batchReadByType(userId, msgType);
        return Result.ok();
    }

    /**
     * 全部消息已读
     * PUT /message/read/all?userId=1005
     */
    @Operation(summary = "全部消息已读")
    @PutMapping("/read/all")
    public Result<Void> readAll() {
        Long userId = 1001L;
        interactionMessageService.readAllMessages(userId);
        return Result.ok();
    }

}
