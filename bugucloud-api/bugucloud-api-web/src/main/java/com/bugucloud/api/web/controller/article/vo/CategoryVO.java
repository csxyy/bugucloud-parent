package com.bugucloud.api.web.controller.article.vo;

import lombok.Data;
import java.util.List;

/**
 * 功能描述: 文章分类VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/27 - 23:15
 */
@Data
public class CategoryVO {

    /** 分类ID */
    private Long id;

    /** 分类名称 */
    private String name;

    /** 该分类下的文章列表 */
    private List<ArticleItemVO> articles;
}
