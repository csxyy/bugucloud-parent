package com.bugucloud.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 功能描述: 文章标签关联表
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:12
 */
@TableName("t_article_tag")
@Data
@Schema(description = "文章标签关联表")
public class ArticleTag extends BaseEntity {

    @Schema(description = "关联文章ID")
    @TableField(value = "article_id")
    private Long articleId;

    @Schema(description = "关联标签ID")
    @TableField(value = "tag_id")
    private Long tagId;
}
