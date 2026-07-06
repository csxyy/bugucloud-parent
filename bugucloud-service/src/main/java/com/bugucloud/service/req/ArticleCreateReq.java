package com.bugucloud.service.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

/**
 * 功能描述: 创建/更新文章请求
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 9:54
 */

@Data
@Schema(description = "创建/更新文章请求")
public class ArticleCreateReq {

    @Schema(description = "文章ID（更新时传，新增时不传）")
    private Long articleId;

    @NotBlank(message = "文章标题不能为空")
    @Size(max = 200, message = "文章标题长度不能超过200位")
    @Schema(description = "文章标题")
    private String title;

    @Size(max = 200, message = "文章摘要长度不能超过200位")
    @Schema(description = "文章摘要")
    private String summary;

    @Schema(description = "标签列表")
    private List<Long> tagIds;

    @Schema(description = "封面图片URL")
    private String cover;

    @Schema(description = "文章发布状态类型 0-草稿 1-已发布")
    private Integer isPublished;

    @NotBlank(message = "文章内容不能为空")
    @Schema(description = "文章内容")
    private String content;
}
