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
    public List<FollowListVO> listMyFollowing(Long userId, Integer followType, String keyword) {
        // 默认查询用户关注
        followType = followType != null ? followType : 1;

        if (followType == 1) {
            // 查询用户关注列表
            List<FollowListVO> followingList = userFollowMapper.selectMyFollowing(userId, keyword);
            return followingList != null ? followingList : Collections.emptyList();
        } else if (followType == 2) {
            // 课程模块正在开发中
            throw new BusinessException("课程模块正在开发中，敬请期待");
        } else {
            throw new BusinessException("不支持的关注类型");
        }
    }

    @Override
    public List<FollowListVO> listMyFollowers(Long userId) {
        List<FollowListVO> followersList = userFollowMapper.selectMyFollowers(userId);
        return followersList != null ? followersList : Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleFollow(Long currentUserId, Long targetUserId, Integer source) {
        // 1. 不能关注自己
        if (currentUserId.equals(targetUserId)) {
            throw new BusinessException("不能关注自己");
        }

        // 2. 查询目标用户是否存在且正常
        User targetUser = userMapper.selectById(targetUserId);
        if (targetUser == null || targetUser.getStatus() != 1) {
            throw new BusinessException("目标用户不存在或已被禁用");
        }

        // 3. 查询是否有关注记录
        UserFollow userFollow = userFollowMapper.selectOne(
                new LambdaQueryWrapper<UserFollow>()
                        .eq(UserFollow::getUserId, currentUserId)
                        .eq(UserFollow::getFollowedUserId, targetUserId)
        );

        if (userFollow == null) {
            // 4. 第一次关注：创建记录
            userFollow = new UserFollow();
            userFollow.setUserId(currentUserId);
            userFollow.setFollowedUserId(targetUserId);
            userFollow.setFollowSource(source != null ? source : 1);
            userFollow.setIsCancel(0);
            userFollowMapper.insert(userFollow);

            // 更新统计：关注者关注数+1，被关注者粉丝数+1
            updateFollowStats(currentUserId, targetUserId, true);

            log.info("关注成功，用户{}关注了用户{}", currentUserId, targetUserId);
        } else {
            // 5. 已有记录，切换关注状态
            if (userFollow.getIsCancel() == 0) {
                // 当前是关注状态 -> 取消关注
                userFollow.setIsCancel(1);
                userFollowMapper.updateById(userFollow);

                // 更新统计：关注者关注数-1，被关注者粉丝数-1
                updateFollowStats(currentUserId, targetUserId, false);

                log.info("取消关注成功，用户{}取消关注用户{}", currentUserId, targetUserId);
            } else {
                // 当前是取消状态 -> 重新关注
                userFollow.setIsCancel(0);
                // 更新关注来源
                if (source != null) {
                    userFollow.setFollowSource(source);
                }
                userFollowMapper.updateById(userFollow);

                // 更新统计：关注者关注数+1，被关注者粉丝数+1
                updateFollowStats(currentUserId, targetUserId, true);

                log.info("重新关注成功，用户{}关注了用户{}", currentUserId, targetUserId);
            }
        }
    }

    /**
     * 更新关注/粉丝数统计
     * @param currentUserId 当前用户ID（关注者）
     * @param targetUserId 目标用户ID（被关注者）
     * @param isFollow true=关注 false=取消关注
     */
    private void updateFollowStats(Long currentUserId, Long targetUserId, boolean isFollow) {
        // 1. 更新当前用户的关注数
        updateUserStat(currentUserId, "follow_count", isFollow);

        // 2. 更新目标用户的粉丝数
        updateUserStat(targetUserId, "follower_count", isFollow);
    }

    /**
     * 更新用户统计字段
     * @param userId 用户ID
     * @param field 统计字段名
     * @param isIncrement true=增加 false=减少
     */
    private void updateUserStat(Long userId, String field, boolean isIncrement) {
        // 先检查用户统计记录是否存在
        UserStat userStat = userStatMapper.selectOne(
                new LambdaQueryWrapper<UserStat>()
                        .eq(UserStat::getUserId, userId)
        );

        if (userStat == null) {
            // 如果统计记录不存在，创建新的
            userStat = new UserStat();
            userStat.setUserId(userId);
            userStat.setTotalViews(0L);
            userStat.setTotalArticles(0);
            userStat.setTotalLikes(0);
            userStat.setTotalCollects(0);
            userStat.setTotalComments(0);
            userStat.setTotalShares(0);
            userStat.setFollowerCount(0);
            userStat.setFollowCount(0);

            // 根据字段设置初始值
            if ("follow_count".equals(field) && isIncrement) {
                userStat.setFollowCount(1);
            } else if ("follower_count".equals(field) && isIncrement) {
                userStat.setFollowerCount(1);
            }

            userStatMapper.insert(userStat);
        } else {
            // 更新统计
            String sql = isIncrement ?
                    field + " = " + field + " + 1" :
                    field + " = GREATEST(" + field + " - 1, 0)";

            userStatMapper.update(null,
                    new LambdaUpdateWrapper<UserStat>()
                            .eq(UserStat::getUserId, userId)
                            .setSql(sql)
            );
        }
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
