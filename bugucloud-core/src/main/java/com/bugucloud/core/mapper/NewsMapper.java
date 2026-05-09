package com.bugucloud.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugucloud.core.entity.News;
import com.bugucloud.core.vo.NewsDetailVO;
import com.bugucloud.core.vo.NewsItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能描述: 资讯Mapper
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:39
 */
public interface NewsMapper extends BaseMapper<News> {

    /**
     * 查询资讯列表
     */
    List<NewsItemVO> selectNewsList();

    /**
     * 查询资讯详情
     */
    NewsDetailVO selectNewsDetailById(@Param("newsId") Long newsId);

}
