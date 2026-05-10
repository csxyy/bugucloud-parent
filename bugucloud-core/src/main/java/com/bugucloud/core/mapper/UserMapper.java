package com.bugucloud.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugucloud.core.entity.User;
import com.bugucloud.core.vo.UserDetailVO;
import org.apache.ibatis.annotations.Param;

/**
 * 功能描述: 用户信息Mapper
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:39
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户个人主页信息
     * @param userId 目标用户ID
     * @param currentUserId 当前登录用户ID（可为null）
     * @return 用户详情
     */
    UserDetailVO selectUserDetail(@Param("userId") Long userId,
                                  @Param("currentUserId") Long currentUserId);

}
