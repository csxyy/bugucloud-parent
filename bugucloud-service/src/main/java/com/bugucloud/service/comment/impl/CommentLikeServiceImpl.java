package com.bugucloud.service.comment.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.core.entity.CommentLike;
import com.bugucloud.core.mapper.CommentLikeMapper;
import com.bugucloud.service.comment.CommentLikeService;
import org.springframework.stereotype.Service;

/**
 * 功能描述: 评论点赞Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Service
public class CommentLikeServiceImpl extends ServiceImpl<CommentLikeMapper, CommentLike> implements CommentLikeService {

}
