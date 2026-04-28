package com.bugucloud.api.web.controller.message;

import com.bugucloud.api.web.controller.message.vo.FollowerVO;
import com.bugucloud.api.web.controller.message.vo.LikeCollectMessageVO;
import com.bugucloud.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 功能描述: 消息通知接口
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 17:09
 */
@RestController
@RequestMapping("/message")
@Tag(name = "消息通知管理")
public class MessageController {
    @Operation(summary = "查询新增粉丝列表")
    @GetMapping("/followers")
    public Result<List<FollowerVO>> listFollowers() {
        return Result.ok();
    }

    @Operation(summary = "查询赞和收藏消息")
    @GetMapping("/like-collect-messages")
    public Result<List<LikeCollectMessageVO>> listLikeCollectMessages() {
        return Result.ok();
    }
}
