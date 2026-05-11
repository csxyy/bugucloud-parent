package com.bugucloud.api.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bugucloud.common.result.Result;
import com.bugucloud.core.vo.CommentVO;
import com.bugucloud.core.vo.MyCommentVO;
import com.bugucloud.service.comment.CommentService;
import com.bugucloud.service.req.CommentCreateReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能描述: 文章评论管理
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 17:09
 */
@RestController
@RequestMapping("/comment")
@Tag(name = "文章评论管理")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 查询文章全部评论列表（包含嵌套子评论）
     */
    @GetMapping("/list/{articleId}")
    @Operation(summary = "查询文章评论列表")
    public Result<List<CommentVO>> getCommentList(
            @Parameter(description = "文章ID") @PathVariable Long articleId) {
        Long currentUserId = 1001L;
        List<CommentVO> commentList = commentService.getCommentList(articleId, currentUserId);
        return Result.ok(commentList);
    }

    /**
     * 创建/回复评论
     */
    @PostMapping("/create")
    @Operation(summary = "创建/回复评论")
    public Result<Long> createComment(@RequestBody @Valid CommentCreateReq req) {
        Long userId = 1001L;
        Long commentId = commentService.createComment(req, userId);
        return Result.ok(commentId);
    }

    /**
     * 点赞/取消点赞评论
     */
    @PostMapping("/like/{commentId}")
    @Operation(summary = "点赞/取消点赞评论")
    public Result<Boolean> likeComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId,
            @Parameter(description = "操作类型 true=点赞 false=取消点赞")
            @RequestParam(required = false, defaultValue = "true") Boolean isLike) {
        Long userId = 1001L;
        Boolean result = commentService.likeComment(commentId, userId, isLike);
        return Result.ok(result);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{commentId}")
    @Operation(summary = "删除评论")
    public Result<Boolean> deleteComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId) {
        Long userId = 1002L;
        Boolean result = commentService.deleteComment(commentId, userId);
        return Result.ok(result);
    }

    /**
     * 管理后台-查询我的评论列表
     */
    @GetMapping("/my")
    @Operation(summary = "查询我的评论列表")
    public Result<IPage<MyCommentVO>> getMyComments(
            @RequestParam(value = "query_type", required = false) Integer queryType,
            @RequestParam(value = "page_num", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        Long userId = 1001L;
        IPage<MyCommentVO> pageResult = commentService.getMyComments(pageNum, pageSize, queryType, userId);
        return Result.ok(pageResult);
    }

}
