package com.bugucloud.service.comment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bugucloud.core.entity.Comment;
import com.bugucloud.core.vo.MineCommentVO;
import com.bugucloud.core.vo.ParentCommentVO;
import com.bugucloud.core.vo.SubCommentVO;
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
     * 查询一级评论列表
     * @param articleId 文章ID
     * @param currentUserId 当前用户ID（可为null）
     * @return 一级评论列表
     */
    List<ParentCommentVO> getParentComments(Long articleId, Long currentUserId);

    /**
     * 查询子评论列表
     * @param rootId 根评论ID（一级评论ID）
     * @param currentUserId 当前用户ID（可为null）
     * @return 子评论列表
     */
    List<SubCommentVO> getChildComments(Long rootId, Long currentUserId);

    /**
     * 创建/回复评论
     */
    void createComment(CommentCreateReq req, Long userId);

    /**
     * 点赞/取消点赞评论（自动切换状态）
     * @param commentId 评论ID
     * @param userId 当前用户ID
     */
    void likeComment(Long commentId, Long userId);

    /**
     * 删除评论（一级评论级联删除子评论）
     * @param commentId 评论ID
     * @param userId 当前用户ID
     */
    void deleteComment(Long commentId, Long userId);

    /**
     * 分页查询别人评论我的文章列表
     * @param userId 当前用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    IPage<MineCommentVO> getMyComments(Long userId, Integer pageNum, Integer pageSize);
}
