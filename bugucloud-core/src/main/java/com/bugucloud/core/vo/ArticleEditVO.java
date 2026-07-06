package com.bugucloud.core.vo;

import lombok.Data;

import java.util.List;

/**
 * 功能描述: 编辑文章响应
 *
 * @author achen
 * @version 1.0
 * @date 2026/7/6 - 14:19
 */
@Data
public class ArticleEditVO {

    /** 文章ID */
    private Long articleId;

    /** 文章标题 */
    private String title;

    /** 文章内容 */
    private String content;

    /** 文章摘要 */
    private String summary;

    /** 文章标签列表 */
    private List<TagVO> tags;

    /** 文章封面URL */
    private String cover;

    /** 作者用户ID */
    private Long userId;
}
