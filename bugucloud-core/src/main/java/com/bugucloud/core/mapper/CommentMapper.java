package com.bugucloud.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bugucloud.core.entity.Comment;
import com.bugucloud.core.vo.CommentVO;
import com.bugucloud.core.vo.MyCommentVO;
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
     * 查询文章的一级评论列表
     */
    List<CommentVO> selectParentComments(@Param("articleId") Long articleId,
                                         @Param("currentUserId") Long currentUserId);

    /**
     * 查询指定父评论的所有子评论
     */
    List<SubCommentVO> selectChildComments(@Param("parentId") Long parentId,
                                           @Param("currentUserId") Long currentUserId);

    /**
     * 插入评论记录
     */
    int insertComment(Comment comment);

    /**
     * 分页查询我的评论列表
     * @param page 分页对象
     * @param userId 当前用户ID
     * @param queryType 查询类型：1-我评论的 2-评论我的 null/0-全部
     * @return 分页结果
     */
    IPage<MyCommentVO> selectMyComments(Page<MyCommentVO> page,
                                        @Param("userId") Long userId,
                                        @Param("queryType") Integer queryType);
}
