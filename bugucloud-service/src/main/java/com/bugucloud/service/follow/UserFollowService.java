package com.bugucloud.service.follow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bugucloud.core.entity.UserFollow;
import com.bugucloud.core.vo.FollowListVO;

import java.util.List;

/**
 * 功能描述: 用户关注Service
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
public interface UserFollowService extends IService<UserFollow> {

    /**
     * 查询我的关注列表
     * @param userId 当前用户ID
     * @param followType 类型字段 用户/课程
     * @param keyword 搜索关键字（可为null）
     * @return 关注列表
     */
    List<FollowListVO> listMyFollowing(Long userId, Integer followType, String keyword);

    /**
     * 查询我的粉丝列表
     * @param userId 当前用户ID
     * @return 粉丝列表
     */
    List<FollowListVO> listMyFollowers(Long userId);

    /**
     * 关注/取消关注用户（自动切换状态）
     * @param currentUserId 当前用户ID（关注者）
     * @param targetUserId 目标用户ID（被关注者）
     * @param source 关注来源 1=博客 2=主页 3=粉丝列表
     */
    void toggleFollow(Long currentUserId, Long targetUserId, Integer source);

    /**
     * 求更新（1天1次）
     * @param currentUserId 当前用户ID（求更新者）
     * @param targetUserId 目标用户ID（被求更新博主）
     */
    void requestUpdate(Long currentUserId, Long targetUserId);

}
