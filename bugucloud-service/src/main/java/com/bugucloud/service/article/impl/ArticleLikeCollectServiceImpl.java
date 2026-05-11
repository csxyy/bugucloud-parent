package com.bugucloud.service.article.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.core.entity.ArticleLikeCollect;
import com.bugucloud.core.mapper.ArticleLikeCollectMapper;
import com.bugucloud.core.vo.LikeCollectMessageVO;
import com.bugucloud.service.article.ArticleLikeCollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 功能描述: 文章点赞收藏Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Service
@RequiredArgsConstructor
public class ArticleLikeCollectServiceImpl extends ServiceImpl<ArticleLikeCollectMapper, ArticleLikeCollect> implements ArticleLikeCollectService {

    private final ArticleLikeCollectMapper articleLikeCollectMapper;

    @Override
    public List<LikeCollectMessageVO> listLikeCollectMessages(Long userId) {
        List<LikeCollectMessageVO> messages = articleLikeCollectMapper.selectLikeCollectMessages(userId);
        return messages != null ? messages : Collections.emptyList();
    }

}
