package com.thirteen.smp.mapper;

import com.thirteen.smp.pojo.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 数据库t_follow表Mapper接口
 *
 * @author 顾建平
 * @version 1.0
 * @since 1.0
 */
public interface FollowMapper {

    /**
     * 通过关注人ID获取关注列表
     * @param followerUserId 关注人ID
     * @return 用户列表
     */
    List<User> selectByFollowerUserId(Integer followerUserId);

    /**
     * 通过被关注人ID粉丝列表
     * @param followedUserId 被关注人ID
     * @return 用户列表
     */
    List<User> selectByFollowedUserId(Integer followedUserId);

    /**
     * 通过关注用户ID和被关注用户ID查询
     * @param followerUserId 关注用户ID
     * @param followedUserId 被关注用户
     * @return 关注结果
     */
    List<Map<String, Object>> selectByUserId(Integer followerUserId, Integer followedUserId);

    /**
     * 新增关注
     * @param followerUserId 关注人ID
     * @param followedUserId 被关注人ID
     * @param followTime     关注时间
     * @return 执行结果
     */
    int insertFollow(Integer followerUserId, Integer followedUserId, Timestamp followTime);

    /**
     * 删除关注
     * @param followerUserId 关注人ID
     * @param followedUserId 被关注人ID
     * @return 执行结果
     */
    int deleteFollow(Integer followerUserId, Integer followedUserId);

}
