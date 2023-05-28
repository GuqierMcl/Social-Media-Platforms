package com.thirteen.smp.service;


import java.util.List;
import java.util.Map;

/**
 * 主页模块业务接口
 */
public interface HomeService {

    /**
     * 获取推荐用户
     * @param count 推荐用户数量
     * @param userId 当前用户ID
     * @return 结果
     */
    List<Map<String,Object>> getRecommendUser(Integer count, Integer userId);

    /**
     * 获取帖子动态
     * @param count 获取动态数量
     * @param userId 当前用户ID
     * @return 结果
     */
    List<Map<String,Object>> getPostUpdate(Integer count, Integer userId);

    /**
     * 获取在线好友
     * @param count 获取好友数量
     * @param userId 当前用户ID
     * @return 结果
     */
    List<Map<String,Object>> getOnlineFriend(Integer count, Integer userId);

}
