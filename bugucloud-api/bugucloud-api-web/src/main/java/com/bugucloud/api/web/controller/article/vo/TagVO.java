package com.bugucloud.api.web.controller.article.vo;

import lombok.Data;

/**
 * 功能描述: 标签VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/27 - 9:43
 */
@Data
public class TagVO {

    /** 标签ID */
    private Long id;

    /** 标签名称 */
    private String name;
}
