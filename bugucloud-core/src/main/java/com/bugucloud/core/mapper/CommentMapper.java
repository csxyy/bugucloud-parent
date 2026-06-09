package com.bugucloud.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bugucloud.core.entity.Comment;
import com.bugucloud.core.vo.MineCommentVO;
import com.bugucloud.core.vo.ParentCommentVO;
import com.bugucloud.core.vo.SubCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能描述: 文章评论Mapper
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:39
 */
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 查询一级评论列表
     * @param articleId 文章ID
     * @param currentUserId 当前用户ID
     * @return 一级评论列表
     */
    List<ParentCommentVO> selectParentComments(@Param("articleId") Long articleId,
                                               @Param("currentUserId") Long currentUserId);

    /**
     * 查询子评论列表
     * @param rootId 根评论ID
     * @param currentUserId 当前用户ID
     * @return 子评论列表
     */
    List<SubCommentVO> selectChildComments(@Param("rootId") Long rootId,
                                           @Param("currentUserId") Long currentUserId);

    /**
     * 插入评论记录
     */
    int insertComment(Comment comment);

    /**
     * 分页查询别人评论我的文章列表
     * @param page 分页对象
     * @param userId 当前用户ID（文章作者）
     * @return 分页结果
     */
    IPage<MineCommentVO> selectMyComments(Page<MineCommentVO> page, @Param("userId") Long userId);
}
