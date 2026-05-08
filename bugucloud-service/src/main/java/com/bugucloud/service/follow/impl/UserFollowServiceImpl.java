package com.bugucloud.service.follow.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.common.exception.BusinessException;
import com.bugucloud.core.entity.User;
import com.bugucloud.core.entity.UserFollow;
import com.bugucloud.core.entity.UserFollowRequest;
import com.bugucloud.core.entity.UserStat;
import com.bugucloud.core.mapper.UserFollowMapper;
import com.bugucloud.core.mapper.UserFollowRequestMapper;
import com.bugucloud.core.mapper.UserMapper;
import com.bugucloud.core.mapper.UserStatMapper;
import com.bugucloud.core.vo.FollowListVO;
import com.bugucloud.service.follow.UserFollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 功能描述: 用户关注Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements UserFollowService {

    private final UserFollowMapper userFollowMapper;
    private final UserMapper userMapper;
    private final UserStatMapper userStatMapper;
    private final UserFollowRequestMapper userFollowRequestMapper;

    @Override
    public List<FollowListVO> listMyFollowing(Long userId) {
        List<FollowListVO> followingList = userFollowMapper.selectMyFollowing(userId);
        return followingList != null ? followingList : Collections.emptyList();
    }

    @Override
    public List<FollowListVO> listMyFollowers(Long userId) {
        List<FollowListVO> followersList = userFollowMapper.selectMyFollowers(userId);
        return followersList != null ? followersList : Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean toggleFollow(Long currentUserId, Long targetUserId, Integer source) {
        // 1. 不能关注自己
        if (currentUserId.equals(targetUserId)) {
            throw new BusinessException("不能关注自己");
        }

        // 2. 查询目标用户是否存在且正常
        User targetUser = userMapper.selectById(targetUserId);
        if (targetUser == null || targetUser.getStatus() != 1) {
            throw new BusinessException("目标用户不存在或已被禁用");
        }

        // 3. 查询是否有关注记录（利用联合主键 user_id + followed_user_id）
        UserFollow userFollow = userFollowMapper.selectOne(
                new LambdaQueryWrapper<UserFollow>()
                        .eq(UserFollow::getUserId, currentUserId)
                        .eq(UserFollow::getFollowedUserId, targetUserId)
        );

        boolean isFollowed;

        if (userFollow == null) {
            // 4. 第一次操作：创建关注记录
            userFollow = new UserFollow();
            userFollow.setUserId(currentUserId);
            userFollow.setFollowedUserId(targetUserId);
            userFollow.setFollowSource(source != null ? source : 1);
            userFollow.setIsCancel(0);
            userFollowMapper.insert(userFollow);

            // 更新粉丝数和关注数 +1
            updateFollowCount(targetUserId, currentUserId, true);

            isFollowed = true;
            log.info("关注成功，用户{}关注了用户{}", currentUserId, targetUserId);
        } else {
            // 5. 已有记录，切换关注状态
            if (userFollow.getIsCancel() == 0) {
                // 当前是关注状态 -> 取消关注
                userFollow.setIsCancel(1);
                userFollowMapper.updateById(userFollow);

                // 更新粉丝数和关注数 -1
                updateFollowCount(targetUserId, currentUserId, false);

                isFollowed = false;
                log.info("取消关注成功，用户{}取消关注用户{}", currentUserId, targetUserId);
            } else {
                // 当前是取消状态 -> 重新关注
                userFollow.setIsCancel(0);
                // 更新关注来源
                if (source != null) {
                    userFollow.setFollowSource(source);
                }
                userFollowMapper.updateById(userFollow);

                // 更新粉丝数和关注数 +1
                updateFollowCount(targetUserId, currentUserId, true);

                isFollowed = true;
                log.info("重新关注成功，用户{}关注了用户{}", currentUserId, targetUserId);
            }
        }

        return isFollowed;
    }

    /**
     * 更新关注/粉丝数统计
     * @param targetUserId 被关注者ID
     * @param currentUserId 关注者ID
     * @param isFollow true=关注 false=取消关注
     */
    private void updateFollowCount(Long targetUserId, Long currentUserId, boolean isFollow) {
        // 更新被关注者的粉丝数
        userStatMapper.update(null,
                new LambdaUpdateWrapper<UserStat>()
                        .eq(UserStat::getUserId, targetUserId)
                        .setSql(isFollow ? "follower_count = follower_count + 1"
                                : "follower_count = GREATEST(follower_count - 1, 0)")
        );

        // 更新关注者的关注数
        userStatMapper.update(null,
                new LambdaUpdateWrapper<UserStat>()
                        .eq(UserStat::getUserId, currentUserId)
                        .setSql(isFollow ? "follow_count = follow_count + 1"
                                : "follow_count = GREATEST(follow_count - 1, 0)")
        );
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void requestUpdate(Long currentUserId, Long targetUserId) {
        // 1. 不能给自己求更新
        if (currentUserId.equals(targetUserId)) {
            throw new BusinessException("不能给自己求更新");
        }

        // 2. 查询目标用户是否存在且正常
        User targetUser = userMapper.selectById(targetUserId);
        if (targetUser == null || targetUser.getStatus() != 1) {
            throw new BusinessException("目标用户不存在或已被禁用");
        }

        // 3. 校验是否已关注（必须关注才能求更新）
        UserFollow userFollow = userFollowMapper.selectOne(
                new LambdaQueryWrapper<UserFollow>()
                        .eq(UserFollow::getUserId, currentUserId)
                        .eq(UserFollow::getFollowedUserId, targetUserId)
                        .eq(UserFollow::getIsCancel, 0)  // 有效关注
        );

        if (userFollow == null) {
            throw new BusinessException("请先关注该用户");
        }

        // 4. 查询是否已有求更新记录
        UserFollowRequest existRequest = userFollowRequestMapper.selectOne(
                new LambdaQueryWrapper<UserFollowRequest>()
                        .eq(UserFollowRequest::getUserId, currentUserId)
                        .eq(UserFollowRequest::getFollowedUserId, targetUserId)
        );

        if (existRequest == null) {
            // 5. 第一次求更新：创建记录
            UserFollowRequest request = new UserFollowRequest();
            request.setUserId(currentUserId);
            request.setFollowedUserId(targetUserId);
            request.setRequestTime(LocalDateTime.now());

            userFollowRequestMapper.insert(request);

            log.info("第一次求更新成功，用户{}向用户{}发送求更新", currentUserId, targetUserId);
        } else {
            // 6. 已有记录：校验今天是否已经求更新过
            LocalDate today = LocalDate.now();
            LocalDate lastRequestDate = existRequest.getRequestTime().toLocalDate();

            if (lastRequestDate.equals(today)) {
                throw new BusinessException("今天已经求更新过了，明天再来吧");
            }

            // 7. 更新时间
            existRequest.setRequestTime(LocalDateTime.now());
            userFollowRequestMapper.updateById(existRequest);

            log.info("求更新成功，用户{}向用户{}发送求更新（更新记录）", currentUserId, targetUserId);
        }
    }

}
