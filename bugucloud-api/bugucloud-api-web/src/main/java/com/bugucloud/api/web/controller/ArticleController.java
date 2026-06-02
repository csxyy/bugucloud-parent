package com.bugucloud.api.web.controller;

import com.bugucloud.common.util.SecurityUtil;
import com.bugucloud.core.vo.*;
import com.bugucloud.service.req.ArticleCreateReq;
import com.bugucloud.common.result.Result;
import com.bugucloud.service.article.ArticleService;
import com.bugucloud.service.req.ArticleLikeCollectReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能描述: 文章信息接口
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 17:09
 */
@RestController
@RequestMapping("/article")
@Tag(name = "文章信息管理")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;


    @Operation(summary = "根据标签ID查询文章列表")
    @GetMapping("/list")
    public Result<List<ArticleItemVO>> getArticleListByTagId(
            @Parameter(description = "标签ID", allowEmptyValue = true)
            @RequestParam(name = "tag_id", required = false) Long tagId) {
        List<ArticleItemVO> list = articleService.getArticleListByTagId(tagId);
        return Result.ok(list);
    }

    // ======================= 文章详情页 ==========================
    // ==================== 接口1：文章核心内容（首屏必需） ====================
    @Operation(summary = "获取文章核心内容")
    @GetMapping("/detail/{articleId}/content")
    public Result<ArticleContentVO> getArticleContent(
            @Parameter(description = "文章ID") @PathVariable Long articleId) {
        ArticleContentVO vo = articleService.getArticleContent(articleId);
        return Result.ok(vo);
    }

    // ==================== 接口2：作者详情 + 侧边栏数据 ====================
    @Operation(summary = "获取文章作者详细信息（右侧栏）")
    @GetMapping("/detail/{articleId}/author")
    public Result<ArticleAuthorDetailVO> getArticleAuthor(
            @Parameter(description = "文章ID") @PathVariable Long articleId) {
        ArticleAuthorDetailVO vo = articleService.getArticleAuthor(articleId);
        return Result.ok(vo);
    }

    // ==================== 接口3：用户交互状态 ====================
    @Operation(summary = "获取当前用户对文章的交互状态")
    @GetMapping("/detail/{articleId}/interaction")
    public Result<ArticleInteractionVO> getInteraction(
            @Parameter(description = "文章ID") @PathVariable Long articleId) {
        Long userId = SecurityUtil.getCurrentUserId();
        ArticleInteractionVO vo = articleService.getInteraction(articleId, userId);
        return Result.ok(vo);
    }

    /**
     * 文章点赞/收藏
     */
    @Operation(summary = "文章点赞/收藏")
    @PostMapping("/like-collect")
    public Result<Void> likeOrCollectArticle(@Valid @RequestBody ArticleLikeCollectReq req) {
        Long userId = SecurityUtil.getCurrentUserId();
        articleService.likeOrCollectArticle(userId, req);
        return Result.ok();
    }

    /**
     * 查询文章管理列表
     */
    @Operation(summary = "查询文章管理列表")
    @GetMapping("/manage")
    public Result<List<ArticleManageVO>> getArticleManageList(
            @Parameter(description = "搜索关键字（标题/摘要）")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "发布状态 0-草稿 1-已发布 2-下架")
            @RequestParam(required = false, name = "is_published") Integer isPublished) {
        Long userId = SecurityUtil.getCurrentUserId();
        List<ArticleManageVO> list = articleService.getArticleManageList(userId, keyword, isPublished);
        return Result.ok(list);
    }

    @Operation(summary = "新增文章")
    @PostMapping("/create")
    public Result<Void> createArticle(@RequestBody @Valid ArticleCreateReq request) {
        Long userId = SecurityUtil.getCurrentUserId();
        articleService.createArticle(request, userId);
        return Result.ok();
    }
}
