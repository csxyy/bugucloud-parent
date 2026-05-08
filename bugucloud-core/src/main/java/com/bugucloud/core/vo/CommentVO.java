package com.bugucloud.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 功能描述: 评论列表响应DTO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 22:17
 */
@Data
public class CommentVO {
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

    /** 子评论列表 */
    private List<SubCommentVO> children;
}
