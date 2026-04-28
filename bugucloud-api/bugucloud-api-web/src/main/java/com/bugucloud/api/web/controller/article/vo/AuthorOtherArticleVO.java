package com.bugucloud.api.web.controller.article.vo;

import lombok.Data;

/**
 * 功能描述: 作者的其他文章VO（只展示标题等核心信息）
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/27 - 9:44
 */
@Data
public class AuthorOtherArticleVO {

    /** 文章ID */
    private Long id;

    /** 文章标题 */
    private String title;
}
