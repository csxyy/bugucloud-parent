package com.bugucloud.service.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bugucloud.core.entity.ArticleLikeCollect;
import com.bugucloud.core.vo.LikeCollectMessageVO;

import java.util.List;

/**
 * 功能描述: 文章点赞收藏Service
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
public interface ArticleLikeCollectService extends IService<ArticleLikeCollect> {

    /**
     * 查询赞和收藏消息列表
     * @param userId 当前用户ID
     * @return 消息列表
     */
    List<LikeCollectMessageVO> listLikeCollectMessages(Long userId);

}
