package com.bugucloud.api.web.controller.news.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 功能描述: 资讯列表项VO
 *
 * @author achen
 * @version 1.0
 * @date 2026/4/27 - 23:21
 */
@Data
public class NewsItemVO {

    /** 资讯ID */
    private Long id;

    /** 资讯标题 */
    private String title;

    /** 资讯类型 */
    private String infoType;

    /** 发布时间 */
    private LocalDateTime createTime;

    /** 封面图片URL */
    private String image;
}
