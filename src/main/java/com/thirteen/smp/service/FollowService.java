package com.thirteen.smp.service;

import java.util.List;
import java.util.Map;

/**
 * 好友模块业务接口
 * @author 顾建平
 */
public interface FollowService {

    /**
     * 获取粉丝信息
     * @param userId 当前用户ID
     * @return 粉丝信息列表
     */
    List<Map<String,Object>> getFansInfo(Integer userId);

    /**
     * 获取关注用户信息
     * @param userId 当前用户ID
     * @return 关注用户信息列表
     */
    List<Map<String,Object>> getFollowedUser(Integer userId);

    /**
     * 关注
     * @param currentUserId 当前用户ID
     * @param targetUserId 目标用户ID
     * @return 执行结果
     */
    boolean follow(Integer currentUserId, Integer targetUserId);

    /**
     * 取消关注
     * @param currentUserId 当前用户ID
     * @param targetUserId 目标用户ID
     * @return 执行结果
     */
    boolean cancelFollow(Integer currentUserId, Integer targetUserId);

}
