package com.bugucloud.service.article.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.core.entity.ArticleTag;
import com.bugucloud.core.mapper.ArticleTagMapper;
import com.bugucloud.service.article.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 功能描述: 文章标签关联Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
