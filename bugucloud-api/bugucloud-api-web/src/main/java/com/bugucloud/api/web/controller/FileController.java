package com.bugucloud.api.web.controller;

import com.bugucloud.common.result.Result;
import com.bugucloud.common.util.OssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FileController {

    private final OssUtil ossUtil;

    @Operation(summary = "上传图片到OSS")
    @PostMapping("/upload-image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        try {
            String url = ossUtil.uploadFile(file);
            return Result.ok(url);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("图片上传失败！");
        }
    }
}
