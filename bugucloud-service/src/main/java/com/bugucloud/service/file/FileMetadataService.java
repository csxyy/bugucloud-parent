package com.bugucloud.service.file;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bugucloud.common.enums.FileTypeEnum;
import com.bugucloud.core.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/7/4 - 23:16
 */
public interface FileMetadataService extends IService<FileMetadata> {
    /**
     * 上传文件
     * @param fileType 文件类型枚举
     * @param relatedId 关联的业务ID（文章ID/资讯ID/用户ID等）
     * @param uploaderId 上传者用户ID
     * @param file 文件
     * @return 文件访问URL
     */
    String uploadFile(FileTypeEnum fileType, Long relatedId, Long uploaderId, MultipartFile file) throws IOException;

    /**
     * 删除文件（软删除）
     * @param fileId 文件ID
     * @param userId 当前用户ID
     * @return 是否成功
     */
    boolean deleteFile(Long fileId, Long userId);
}
