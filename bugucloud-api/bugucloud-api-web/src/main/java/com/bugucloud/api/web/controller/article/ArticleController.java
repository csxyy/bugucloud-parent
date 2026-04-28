package com.bugucloud.api.web.controller.article;

import com.bugucloud.api.web.controller.article.req.ArticleCreateReq;
import com.bugucloud.api.web.controller.article.vo.ArticleDetailVO;
import com.bugucloud.api.web.controller.article.vo.ArticleManageVO;
import com.bugucloud.api.web.controller.article.vo.CategoryVO;
import com.bugucloud.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class ArticleController {

    @Operation(summary = "根据文章ID查询文章详情")
    @GetMapping("/articles/{articleId}")
    public Result<ArticleDetailVO> getArticleDetail(
            @Parameter(description = "文章ID") @PathVariable Long articleId) {
        return Result.ok();
    }

    @Operation(summary = "查询文章管理列表")
    @GetMapping("/articles")
    public Result<List<ArticleManageVO>> listArticles() {
        return Result.ok();
    }

    @Operation(summary = "发布文章")
    @PostMapping("/articles")
    public Result<Void> createArticle(@Valid @RequestBody ArticleCreateReq request) {

        return Result.ok();
    }
}
