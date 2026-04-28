package com.bugucloud.api.web.controller.tag;

import com.bugucloud.api.web.controller.article.vo.CategoryVO;
import com.bugucloud.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能描述: 标签接口
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 17:09
 */
@RestController
@RequestMapping("/tag")
@Tag(name = "标签管理")
public class TagController {
    @Operation(summary = "查询所有标签分类")
    @GetMapping("/list")
    public Result<List<CategoryVO>> listTags() {
        return Result.ok();
    }

    @Operation(summary = "根据分类ID查询文章列表")
    @GetMapping("/{categoryId}")
    public Result<CategoryVO> getCategoryById(
            @Parameter(description = "分类ID") @PathVariable Long categoryId) {
        return Result.ok();
    }
}
