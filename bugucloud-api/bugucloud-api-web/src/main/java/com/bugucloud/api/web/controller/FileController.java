package com.bugucloud.api.web.controller;

import com.bugucloud.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 功能描述: 文件上传控制器
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 10:34
 */

@Tag(name = "文件管理")
@RestController
@RequestMapping("/file")
public class FileController {

    @Operation(summary = "上传图片到OSS")
    @PostMapping("/upload-image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        return Result.ok();
    }
}
