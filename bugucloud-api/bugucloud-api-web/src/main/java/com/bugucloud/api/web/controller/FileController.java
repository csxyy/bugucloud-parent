package com.bugucloud.api.web.controller;

import com.bugucloud.common.enums.FileTypeEnum;
import com.bugucloud.common.result.Result;
import com.bugucloud.common.util.OssUtil;
import com.bugucloud.common.util.SecurityUtil;
import com.bugucloud.service.file.FileMetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 功能描述: 文件上传控制器
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 10:34
 */
@Slf4j
@Tag(name = "文件管理")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileMetadataService fileMetadataService;

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public Result<String> uploadFile(
            @Parameter(description = "文件", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "文件类型：1-文章配图，2-用户头像，3-资讯图片", required = true)
            @RequestParam("fileType") Integer fileTypeCode,
            @Parameter(description = "关联的业务ID（文章ID/资讯ID），用户头像时可不传")
            @RequestParam(value = "relatedId", required = false) Long relatedId) {

        // 1. 参数校验
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        // 2. 获取当前用户ID（一开始就获取）
        Long userId = SecurityUtil.getCurrentUserId();

        // 3. 根据数字获取枚举
        FileTypeEnum fileType;
        try {
            fileType = FileTypeEnum.fromCode(fileTypeCode);
        } catch (IllegalArgumentException e) {
            log.warn("不支持的文件类型: {}", fileTypeCode);
            return Result.error("不支持的文件类型: " + fileTypeCode);
        }

        // 4. 处理 relatedId
        // 如果是用户头像，使用当前用户ID作为 relatedId
        if (fileType == FileTypeEnum.USER_AVATAR) {
            relatedId = userId;
            log.info("上传用户头像，用户ID: {}", userId);
        } else {
            // 文章或资讯图片，必须传 relatedId
            if (relatedId == null) {
                return Result.error("文章/资讯图片必须传入关联ID(relatedId)");
            }
            if (relatedId <= 0) {
                return Result.error("关联ID不能小于0");
            }
            log.info("上传{}，关联ID: {}, 用户ID: {}", fileType.getDesc(), relatedId, userId);
        }

        // 5. 调用Service上传
        try {
            String url = fileMetadataService.uploadFile(fileType, relatedId, userId, file);
            return Result.ok(url);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除文件（软删除）")
    @DeleteMapping("/{fileId}")
    public Result<Boolean> deleteFile(@PathVariable Long fileId) {
        // 获取当前用户ID
        Long userId;
        try {
            userId = SecurityUtil.getCurrentUserId();
        } catch (Exception e) {
            log.error("获取当前用户失败", e);
            return Result.error("用户未登录或登录已过期");
        }

        try {
            boolean result = fileMetadataService.deleteFile(fileId, userId);
            return Result.ok(result);
        } catch (Exception e) {
            log.error("文件删除失败", e);
            return Result.error("文件删除失败: " + e.getMessage());
        }
    }
}
