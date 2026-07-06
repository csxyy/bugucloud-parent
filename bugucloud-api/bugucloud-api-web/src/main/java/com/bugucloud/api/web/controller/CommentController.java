package com.bugucloud.api.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bugucloud.common.result.Result;
import com.bugucloud.common.util.SecurityUtil;
import com.bugucloud.core.vo.MineCommentVO;
import com.bugucloud.core.vo.ParentCommentVO;
import com.bugucloud.core.vo.SubCommentVO;
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
     * 查询一级评论列表
     */
    @Operation(summary = "查询一级评论列表")
    @GetMapping("/parent/{articleId}")
    public Result<List<ParentCommentVO>> getParentComments(
            @Parameter(description = "文章ID") @PathVariable Long articleId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        List<ParentCommentVO> commentList = commentService.getParentComments(articleId, currentUserId);
        return Result.ok(commentList);
    }

    /**
     * 查询子评论列表
     */
    @Operation(summary = "查询子评论列表")
    @GetMapping("/child/{rootId}")
    public Result<List<SubCommentVO>> getChildComments(
            @Parameter(description = "根评论ID（一级评论ID）") @PathVariable Long rootId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        List<SubCommentVO> childCommentList = commentService.getChildComments(rootId, currentUserId);
        return Result.ok(childCommentList);
    }

    /**
     * 创建/回复评论
     */
    @PostMapping("/create")
    @Operation(summary = "创建/回复评论")
    public Result<Void> createComment(@RequestBody @Valid CommentCreateReq req) {
        Long userId = SecurityUtil.getCurrentUserId();
        commentService.createComment(req, userId);
        return Result.ok();
    }

    /**
     * 点赞/取消点赞评论
     */
    @PostMapping("/like/{commentId}")
    @Operation(summary = "点赞/取消点赞评论")
    public Result<Void> likeComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId) {
        Long userId = SecurityUtil.getCurrentUserId();
        commentService.likeComment(commentId, userId);
        return Result.ok();
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{commentId}")
    @Operation(summary = "删除评论")
    public Result<Void> deleteComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId) {
        Long userId = SecurityUtil.getCurrentUserId();
        commentService.deleteComment(commentId, userId);
        return Result.ok();
    }

    /**
     * 管理后台-查询我的评论列表
     */
    @Operation(summary = "查询我的评论列表")
    @GetMapping("/my-comments")
    public Result<IPage<MineCommentVO>> getMyComments(
            @Parameter(description = "页码")
            @RequestParam(name = "page_num", defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小")
            @RequestParam(name = "page_size", defaultValue = "8") Integer pageSize) {
        Long userId = SecurityUtil.getCurrentUserId();
        IPage<MineCommentVO> pageResult = commentService.getMyComments(userId, pageNum, pageSize);
        return Result.ok(pageResult);
    }

}
