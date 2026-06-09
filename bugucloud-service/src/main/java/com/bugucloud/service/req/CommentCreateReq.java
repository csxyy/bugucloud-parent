package com.bugucloud.service.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 功能描述: 创建评论请
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 22:20
 */
@Data
@Schema(description = "创建评论请求")
public class CommentCreateReq {
    @NotNull(message = "文章ID不能为空")
    @Schema(description = "文章ID")
    private Long articleId;

    @Schema(description = "根评论ID（0表示一级评论）")
    private Long rootId;

    @Schema(description = "父评论ID（0表示一级评论）")
    private Long parentId;

    @Schema(description = "被回复用户ID")
    private Long replyUserId;

    @NotBlank(message = "评论内容不能为空")
    @Schema(description = "评论内容")
    private String content;
}
