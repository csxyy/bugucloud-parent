package com.bugucloud.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugucloud.core.entity.User;
import com.bugucloud.core.vo.UserArticleVO;
import com.bugucloud.core.vo.UserDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能描述: 用户信息Mapper
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:39
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户个人主页信息（包含个人成就）
     */
    UserDetailVO selectUserDetail(@Param("userId") Long userId,
                                  @Param("currentUserId") Long currentUserId);

    /**
     * 查询用户的文章列表
     */
    List<UserArticleVO> selectUserArticles(@Param("userId") Long userId);

}
