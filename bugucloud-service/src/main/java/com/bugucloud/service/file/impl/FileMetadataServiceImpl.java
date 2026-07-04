package com.bugucloud.service.file.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bugucloud.common.enums.FileTypeEnum;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.common.util.OssUtil;
import com.bugucloud.common.util.SecurityUtil;
import com.bugucloud.core.entity.FileMetadata;
import com.bugucloud.core.mapper.FileMetadataMapper;
import com.bugucloud.service.file.FileMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/7/4 - 23:16
 */


@Slf4j
@Service
@RequiredArgsConstructor
public class FileMetadataServiceImpl extends ServiceImpl<FileMetadataMapper, FileMetadata> implements FileMetadataService {

    private final OssUtil ossUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadFile(FileTypeEnum fileType, Long relatedId, Long uploaderId, MultipartFile file) throws IOException {
        // 1. 生成文件名（时间戳 + UUID + 后缀）
        String originalFilename = file.getOriginalFilename();
        String fileName = generateFileName(originalFilename);

        // 2. 构建OSS路径：通过枚举的buildOssKey方法
        String ossKey = fileType.buildOssKey(relatedId, fileName);

        // 3. 上传文件到OSS
        String fileUrl = ossUtil.uploadFile(ossKey, file);

        // 4. 保存文件元数据到数据库
        FileMetadata metadata = buildFileMetadata(ossKey, fileUrl, file, fileType, relatedId, uploaderId);
        save(metadata);

        log.info("文件上传成功, 类型: {}, 关联ID: {}, 上传者: {}, OSS路径: {}, URL: {}",
                fileType.getDesc(), relatedId, uploaderId, ossKey, fileUrl);
        return fileUrl;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFile(Long fileId, Long userId) {
        LambdaQueryWrapper<FileMetadata> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FileMetadata::getId, fileId)
                .eq(FileMetadata::getUploaderId, userId)
                .eq(FileMetadata::getIsDeleted, 0);

        FileMetadata metadata = getOne(wrapper);
        if (metadata == null) {
            throw new BusinessException("文件不存在或无权删除");
        }

        // 软删除：更新删除标记和删除时间
        metadata.setIsDeleted(1);
        metadata.setDeletedAt(LocalDateTime.now());
        boolean updated = updateById(metadata);

        if (updated) {
            log.info("文件软删除成功, 文件ID: {}, OSS路径: {}", fileId, metadata.getOssKey());
        }
        return updated;
    }

    /**
     * 构建文件元数据对象
     */
    private FileMetadata buildFileMetadata(String ossKey, String fileUrl, MultipartFile file,
                                           FileTypeEnum fileType, Long relatedId, Long uploaderId) throws IOException {
        FileMetadata metadata = new FileMetadata();
        metadata.setOssKey(ossKey);
        metadata.setFileUrl(fileUrl);
        metadata.setFileName(file.getOriginalFilename());
        metadata.setFileSize(file.getSize());
        metadata.setMimeType(file.getContentType());
        metadata.setFileType(fileType);
        metadata.setUploaderId(uploaderId);
        metadata.setRelatedId(relatedId);
        metadata.setIsDeleted(0);
        return metadata;
    }

    /**
     * 生成文件名（时间戳 + UUID + 后缀）
     */
    private String generateFileName(String originalFilename) {
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace("-", "") + suffix;
    }
}
