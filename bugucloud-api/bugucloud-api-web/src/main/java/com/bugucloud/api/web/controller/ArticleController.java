package com.bugucloud.api.web.controller;

import com.bugucloud.common.util.SecurityUtil;
import com.bugucloud.core.vo.ArticleDetailVO;
import com.bugucloud.core.vo.ArticleItemVO;
import com.bugucloud.core.vo.ArticleManageVO;
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

    @Operation(summary = "根据文章ID查询文章详情")
    @GetMapping("/detail/{articleId}")
    public Result<ArticleDetailVO> getArticleDetail(
            @Parameter(description = "文章ID") @PathVariable Long articleId) {
        Long userId = SecurityUtil.getCurrentUserId();
        ArticleDetailVO articleDetailDTO = articleService.getArticleDetailById(articleId, userId);
        return Result.ok(articleDetailDTO);
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
