package com.bugucloud.api.web.controller;

import com.bugucloud.common.result.Result;
import com.bugucloud.core.vo.TagVO;
import com.bugucloud.service.tag.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "查询所有标签分类")
    @GetMapping("/list")
    public Result<List<TagVO>> listTags() {
        List<TagVO> tags = tagService.listTags();
        return Result.ok(tags);
    }

}
