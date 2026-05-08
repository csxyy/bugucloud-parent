package com.bugucloud.api.web.controller.tag;

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
//    @Operation(summary = "查询所有标签分类")
//    @GetMapping("/list")
//    public Result<List<CategoryDTO>> listTags() {
//        return Result.ok();
//    }

}
