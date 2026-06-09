package com.bugucloud.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 功能描述: 一级评论
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 22:17
 */
@Data
public class ParentCommentVO {

    /** 评论ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 用户头像URL */
    private String avatar;

    /** 用户昵称 */
    private String nickname;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "GMT+8")
    private LocalDateTime createTime;

    /** 评论内容 */
    private String content;

    /** 是否是作者 */
    private Boolean isAuthor;

    /** 点赞数 */
    private Integer likes;

    /** 是否点赞 */
    private Boolean isLiked;

    /** 子评论数 */
    private Integer subCommentCount;
}
