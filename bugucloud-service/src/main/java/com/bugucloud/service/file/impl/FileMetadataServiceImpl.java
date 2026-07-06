package com.bugucloud.service.file.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bugucloud.common.enums.FileTypeEnum;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.common.util.OssUtil;
import com.bugucloud.common.util.SecurityUtil;
import com.bugucloud.core.entity.FileMetadata;
import com.bugucloud.core.mapper.FileMetadataMapper;
import com.bugucloud.service.file.FileMetadataService;
import com.bugucloud.service.user.UserService;
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

    private final UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadFile(FileTypeEnum fileType, Long relatedId, Long uploaderId, MultipartFile file) throws IOException {
        // 1. 生成文件名（时间戳 + UUID + 后缀）
        String originalFilename = file.getOriginalFilename();
        String fileName = generateFileName(originalFilename);

        // 2. 处理 relatedId：如果是文章封面且 relatedId 为空，自动生成雪花ID
        if (fileType == FileTypeEnum.ARTICLE_COVER && relatedId == null) {
            // 使用 MyBatis-Plus 的雪花算法生成ID
            relatedId = IdWorker.getId();
            log.info("文章封面自动生成业务ID: {}", relatedId);
        }

        // 3. 构建OSS路径：通过枚举的buildOssKey方法
        String ossKey = fileType.buildOssKey(relatedId, fileName);

        // 4. 上传文件到OSS
        String fileUrl = ossUtil.uploadFile(ossKey, file);

        // 5. 保存文件元数据到数据库
        FileMetadata metadata = buildFileMetadata(ossKey, fileUrl, file, fileType, relatedId, uploaderId);
        save(metadata);

        // 6. 如果是用户头像，同步更新用户表的 avatar 字段
        if (fileType == FileTypeEnum.USER_AVATAR) {
            userService.updateAvatar(uploaderId, fileUrl);
            log.info("用户头像更新成功, 用户ID: {}, URL: {}", uploaderId, fileUrl);
        }

        log.info("文件上传成功, 类型: {}, 关联ID: {}, 上传者: {}, OSS路径: {}, URL: {}",
                fileType.getDesc(), relatedId, uploaderId, ossKey, fileUrl);
        return fileUrl;
    }

    @Override
    public Long getLatestCoverRelatedId(Long userId) {
        // 查询当前用户最新上传的、未关联文章的封面文件
        // 条件：用户ID、文件类型为文章封面、未删除、且关联ID对应的文章不存在（即还未创建文章）
        LambdaQueryWrapper<FileMetadata> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FileMetadata::getUploaderId, userId)
                .eq(FileMetadata::getFileType, FileTypeEnum.ARTICLE_COVER)
                .eq(FileMetadata::getIsDeleted, 0)
                // 关联ID不为空（上传时自动生成的雪花ID）
                .isNotNull(FileMetadata::getRelatedId)
                .orderByDesc(FileMetadata::getCreateTime)
                .last("LIMIT 1");

        FileMetadata metadata = getOne(wrapper);
        if (metadata == null) {
            log.info("当前用户没有待关联的封面文件, 用户ID: {}", userId);
            return null;
        }

        // 获取关联ID（自动生成的雪花ID）
        Long relatedId = metadata.getRelatedId();
        log.info("获取到待关联的封面业务ID: {}, 用户ID: {}", relatedId, userId);
        return relatedId;
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
