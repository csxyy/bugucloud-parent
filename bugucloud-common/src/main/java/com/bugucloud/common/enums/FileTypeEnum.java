package com.bugucloud.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能描述: 文件类型枚举
 *
 * @author achen
 * @version 1.0
 * @date 2026/7/4 - 23:09
 */

@Getter
@AllArgsConstructor
public enum FileTypeEnum {

    ARTICLE_IMAGE(1, "文章配图", "articles", true),
    USER_AVATAR(2, "用户头像", "users", false),
    NEWS_IMAGE(3, "资讯图片", "news", true),
    // 新增封面类型，不需要传relatedId
    ARTICLE_COVER(4, "文章封面", "articles", false);

    /**
     * 数据库存储值
     */
    @EnumValue  // MyBatis-Plus注解，标注数据库中存储的值
    private final Integer code;

    /**
     * 描述信息
     */
    @JsonValue  // Jackson注解，序列化时返回code
    private final String desc;

    /**
     * OSS目录前缀
     */
    private final String prefix;

    /**
     * 是否需要业务ID（true: 必须传，false: 自动生成）
     */
    private final boolean needRelatedId;

    /**
     * 根据code获取枚举
     */
    public static FileTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (FileTypeEnum type : FileTypeEnum.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的文件类型: " + code);
    }

    /**
     * 构建OSS路径
     * @param relatedId 关联的业务ID
     * @param fileName 文件名
     * @return OSS完整路径
     */
    public String buildOssKey(Long relatedId, String fileName) {
        return this.prefix + "/" + relatedId + "/" + fileName;
    }

    /**
     * 判断是否为图片类型
     */
    public boolean isImage() {
        return this == ARTICLE_IMAGE || this == USER_AVATAR || this == NEWS_IMAGE;
    }
}
