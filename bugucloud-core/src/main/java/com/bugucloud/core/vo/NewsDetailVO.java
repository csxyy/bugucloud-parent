package com.bugucloud.core.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能描述: 资讯详情VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/27 - 23:22
 */

@Data
public class NewsDetailVO {

    /** 资讯ID */
    private Long id;

    /** 资讯标题 */
    private String title;

    /** 资讯内容 */
    private String content;

    /** 资讯类型 */
    private String infoType;

    /** 发布时间 */
    private LocalDateTime createTime;

    /** 封面图片URL */
    private String image;
}
