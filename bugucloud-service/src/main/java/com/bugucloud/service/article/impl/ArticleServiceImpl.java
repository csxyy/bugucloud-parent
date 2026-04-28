package com.bugucloud.service.article.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.core.entity.Article;
import com.bugucloud.core.mapper.ArticleMapper;
import com.bugucloud.service.article.ArticleService;
import org.springframework.stereotype.Service;

/**
 * 功能描述: 文章信息Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

}
