package com.bugucloud.service.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 功能描述: 文章互动操作请求（点赞/收藏）
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/28 - 22:51
 */
@Data
@Schema(description = "文章点赞/收藏请求")
public class ArticleLikeCollectReq {

    @NotNull(message = "文章ID不能为空")
    @Schema(description = "文章ID")
    private Long articleId;

    @NotNull(message = "操作类型不能为空")
    @Schema(description = "操作类型 1=点赞 2=收藏")
    private Integer msgType;

    @NotNull(message = "操作状态不能为空")
    @Schema(description = "操作状态 true=触发操作 false=取消操作")
    private Boolean isActive;
}
