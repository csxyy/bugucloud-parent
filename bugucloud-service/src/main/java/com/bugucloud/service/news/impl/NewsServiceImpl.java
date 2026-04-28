package com.bugucloud.service.news.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.core.entity.News;
import com.bugucloud.core.mapper.NewsMapper;
import com.bugucloud.service.news.NewsService;
import org.springframework.stereotype.Service;

/**
 * 功能描述: 资讯Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

}
