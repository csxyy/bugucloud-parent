package com.bugucloud.service.news.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.core.entity.News;
import com.bugucloud.core.mapper.NewsMapper;
import com.bugucloud.core.vo.NewsDetailVO;
import com.bugucloud.core.vo.NewsItemVO;
import com.bugucloud.service.news.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 功能描述: 资讯Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Service
@RequiredArgsConstructor
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    private final NewsMapper newsMapper;

    @Override
    public IPage<NewsItemVO> listNews(Integer pageNum, Integer pageSize) {
        // 创建分页对象
        Page<NewsItemVO> page = new Page<>(pageNum, pageSize);

        // 分页查询
        IPage<NewsItemVO> newsPage = newsMapper.selectNewsPage(page);

        return newsPage;
    }

    @Override
    public NewsDetailVO getNewsDetail(Long newsId) {
        NewsDetailVO newsDetail = newsMapper.selectNewsDetailById(newsId);
        if (newsDetail == null) {
            throw new BusinessException("资讯不存在");
        }
        return newsDetail;
    }

}
