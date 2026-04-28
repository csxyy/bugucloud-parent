package com.bugucloud.service.comment.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.core.entity.Comment;
import com.bugucloud.core.mapper.CommentMapper;
import com.bugucloud.service.comment.CommentService;
import org.springframework.stereotype.Service;

/**
 * 功能描述: 文章评论Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
