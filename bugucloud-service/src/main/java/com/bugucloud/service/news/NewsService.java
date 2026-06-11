package com.bugucloud.service.news;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bugucloud.core.entity.News;
import com.bugucloud.core.vo.NewsDetailVO;
import com.bugucloud.core.vo.NewsItemVO;

import java.util.List;

/**
 * 功能描述: 资讯Service
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
public interface NewsService extends IService<News> {


    /**
     * 分页查询资讯列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    IPage<NewsItemVO> listNews(Integer pageNum, Integer pageSize);

    /**
     * 查询资讯详情
     */
    NewsDetailVO getNewsDetail(Long newsId);

}
