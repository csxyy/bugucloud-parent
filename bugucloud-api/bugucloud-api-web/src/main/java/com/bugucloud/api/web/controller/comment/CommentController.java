package com.bugucloud.api.web.controller.comment;

import com.bugucloud.api.web.controller.comment.vo.CommentItemVO;
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
 * 功能描述: 文章评论接口
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 17:09
 */
@RestController
@RequestMapping("/comment")
@Tag(name = "文章评论管理")
public class CommentController {

    @Operation(summary = "查询评论列表")
    @GetMapping("/comments")
    public Result<List<CommentItemVO>> listComments() {
        return Result.ok();
    }

}
