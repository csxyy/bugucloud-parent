package com.bugucloud.service.comment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bugucloud.core.entity.Comment;
import com.bugucloud.core.vo.CommentVO;
import com.bugucloud.core.vo.MyCommentVO;
import com.bugucloud.service.req.CommentCreateReq;

import java.util.List;

/**
 * 功能描述: 文章评论Service
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
public interface CommentService extends IService<Comment> {
    /**
     * 查询文章评论列表（含嵌套子评论）
     */
    List<CommentVO> getCommentList(Long articleId, Long currentUserId);

    /**
     * 创建/回复评论
     */
    Long createComment(CommentCreateReq req, Long userId);

    /**
     * 点赞/取消点赞评论
     * @param commentId 评论ID
     * @param userId 用户ID
     * @param isLike true=点赞 false=取消点赞
     * @return 操作结果
     */
    Boolean likeComment(Long commentId, Long userId, Boolean isLike);

    /**
     * 删除评论（逻辑删除）
     * @param commentId 评论ID
     * @param userId 当前用户ID
     * @return 操作结果
     */
    Boolean deleteComment(Long commentId, Long userId);

    /**
     * 分页查询我的评论列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param queryType 查询类型：null/0-全部 1-我评论的 2-评论我的
     * @param userId 当前用户ID
     * @return 分页结果
     */
    IPage<MyCommentVO> getMyComments(Integer pageNum, Integer pageSize,
                                     Integer queryType, Long userId);
}
