package com.bugucloud.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugucloud.core.entity.UserFollow;
import com.bugucloud.core.vo.FollowListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能描述: 用户关注Mapper
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16 - 15:39
 */
public interface UserFollowMapper extends BaseMapper<UserFollow> {

    /**
     * 查询我的关注列表（包含互关状态）
     * @param userId 当前用户ID（我）
     * @return 关注列表
     */
    List<FollowListVO> selectMyFollowing(@Param("userId") Long userId);

    /**
     * 查询我的粉丝列表（包含我是否关注了对方）
     * @param userId 当前用户ID（我）
     * @return 粉丝列表
     */
    List<FollowListVO> selectMyFollowers(@Param("userId") Long userId);

}
