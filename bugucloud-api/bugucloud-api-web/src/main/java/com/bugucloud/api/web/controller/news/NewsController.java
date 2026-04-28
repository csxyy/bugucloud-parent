package com.bugucloud.api.web.controller.news;

import com.bugucloud.api.web.controller.news.vo.NewsDetailVO;
import com.bugucloud.api.web.controller.news.vo.NewsItemVO;
import com.bugucloud.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class NewsController {

    @Operation(summary = "查询资讯列表")
    @GetMapping("/list")
    public Result<List<NewsItemVO>> listNews() {
        return Result.ok();
    }

    @Operation(summary = "查询单个资讯详情")
    @GetMapping("/{newsId}")
    public Result<NewsDetailVO> getNewsDetail(
            @Parameter(description = "资讯ID") @PathVariable Long newsId) {
        return Result.ok();
    }
}
