package com.bugucloud.api.web.controller;

import com.bugucloud.common.util.SecurityUtil;
import com.bugucloud.core.vo.*;
import com.bugucloud.service.req.ArticleCreateReq;
import com.bugucloud.common.result.Result;
import com.bugucloud.service.article.ArticleService;
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

    @Operation(summary = "查询文章管理列表")
    @GetMapping("/manage")
    public Result<List<ArticleManageVO>> getArticleManageList() {
        Long userId = 1002L;
        List<ArticleManageVO> list = articleService.getArticleManageListByUserId(userId);
        return Result.ok(list);
    }

    @Operation(summary = "新增文章")
    @PostMapping("/create")
    public Result<Void> createArticle(@RequestBody @Valid ArticleCreateReq request) {
        Long userId = 1002L;
        Long article = articleService.createArticle(request, userId);
        System.out.println(article);
        return Result.ok();
    }
}
