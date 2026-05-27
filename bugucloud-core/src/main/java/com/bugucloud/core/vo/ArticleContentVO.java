package com.bugucloud.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 功能描述: 文章核心内容 VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/27 - 22:27
 */
@Data
public class ArticleContentVO {
    /** 文章ID */
    private Long id;

    /** 文章标题 */
    private String title;

    /** 文章内容（完整富文本） */
    private String content;

    /** 发布时间 */
    private LocalDateTime createTime;

    /** 阅读数 */
    private Long views;

    /** 点赞数 */
    private Integer likes;

    /** 收藏数 */
    private Integer collects;

    /** 文章标签列表 */
    private List<TagVO> tags;

    /** 底部栏作者简要信息（仅头像+昵称） */
    private AuthorBriefVO authorBrief;
}
