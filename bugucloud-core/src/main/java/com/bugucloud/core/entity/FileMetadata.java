package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.bugucloud.common.enums.FileTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 功能描述: 文件元数据实体
 *
 * @author achen
 * @version 1.0
 * @date 2026/7/4 - 23:12
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_file_metadata")
public class FileMetadata extends BaseEntity{
    /**
     * OSS完整路径，如 community/articles/123/cover.jpg
     */
    @TableField("oss_key")
    private String ossKey;

    /**
     * 文件访问URL（带签名的临时链接或CDN链接）
     */
    @TableField("file_url")
    private String fileUrl;

    /**
     * 用户上传时的原始文件名
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件大小，单位字节
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * MIME类型，如 image/png、image/jpeg
     */
    @TableField("mime_type")
    private String mimeType;

    /**
     * 文件类型：1-文章配图，2-用户头像，3-资讯图片
     *
     * MyBatis-Plus会自动将枚举的code值存入数据库
     * 查询时也会自动将数据库的数字转换为枚举对象
     */
    @TableField("file_type")
    private FileTypeEnum fileType;

    /**
     * 上传者用户ID
     */
    @TableField("uploader_id")
    private Long uploaderId;

    /**
     * 关联的业务ID（文章ID/用户ID/资讯ID）
     */
    @TableField("related_id")
    private Long relatedId;

    /**
     * 扩展信息，如图片宽高、视频时长等
     * 使用 Jackson 的 JsonNode 或 String 类型存储
     */
    @TableField("extra")
    private String extra;

    /**
     * 软删除标记：0-未删除，1-已删除
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 删除时间（用于延迟清理）
     */
    @TableField("deleted_at")
    private LocalDateTime deletedAt;
}
