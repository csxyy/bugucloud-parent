package com.bugucloud.api.web.controller.news;

import com.bugucloud.common.result.Result;
import com.bugucloud.core.vo.NewsDetailVO;
import com.bugucloud.core.vo.NewsItemVO;
import com.bugucloud.service.news.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能描述: 资讯接口
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 17:09
 */
@RestController
@RequestMapping("/news")
@Tag(name = "资讯管理")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "查询资讯列表")
    @GetMapping("/list")
    public Result<List<NewsItemVO>> listNews() {
        List<NewsItemVO> newsList = newsService.listNews();
        return Result.ok(newsList);
    }

    @Operation(summary = "查询单个资讯详情")
    @GetMapping("/{newsId}")
    public Result<NewsDetailVO> getNewsDetail(
            @Parameter(description = "资讯ID") @PathVariable Long newsId) {
        NewsDetailVO newsDetail = newsService.getNewsDetail(newsId);
        return Result.ok(newsDetail);
    }
}
