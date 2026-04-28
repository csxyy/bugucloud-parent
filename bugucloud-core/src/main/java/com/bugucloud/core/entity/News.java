package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 功能描述: 资讯表（纯引流、与用户无关）
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:12
 */
@TableName("t_news")
@Data
@Schema(description = "资讯表（纯引流、与用户无关）")
public class News extends BaseEntity {

    @Schema(description = "资讯标题")
    @TableField(value = "title")
    private String title;

    @Schema(description = "资讯封面图片URL")
    @TableField(value = "image")
    private String image;

    @Schema(description = "资讯类型")
    @TableField(value = "info_type")
    private String infoType;

    @Schema(description = "资讯内容")
    @TableField(value = "content")
    private String content;
}
