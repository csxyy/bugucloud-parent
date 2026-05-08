package com.bugucloud.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 功能描述: 文章详情DTO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/28 - 14:43
 */
@Data
public class ArticleDetailVO {
    /** 文章ID */
    private Long id;

    /** 文章标题 */
    private String title;

    /** 文章内容（完整内容） */
    private String content;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime createTime;

    /** 阅读数 */
    private Long views;

    /** 点赞数 */
    private Integer likes;

    /** 收藏数 */
    private Integer collects;

    /** 文章标签列表 */
    private List<TagVO> tags;

    /** 作者信息 */
    private ArticleAuthorVO author;

    /** 当前登录用户是否点赞 */
    private Boolean isLiked;

    /** 当前登录用户是否收藏 */
    private Boolean isCollected;

    /** 当前登录用户是否关注了作者 */
    private Boolean isFollowed;
}
