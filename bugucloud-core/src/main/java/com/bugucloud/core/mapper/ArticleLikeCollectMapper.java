package com.bugucloud.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugucloud.core.entity.ArticleLikeCollect;
import com.bugucloud.core.vo.LikeCollectMessageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能描述: 文章点赞收藏Mapper
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:39
 */
public interface ArticleLikeCollectMapper extends BaseMapper<ArticleLikeCollect> {

    /**
     * 查询别人对我的文章的点赞和收藏消息
     * @param userId 当前用户ID（文章作者）
     * @return 消息列表
     */
    List<LikeCollectMessageVO> selectLikeCollectMessages(@Param("userId") Long userId);

}
