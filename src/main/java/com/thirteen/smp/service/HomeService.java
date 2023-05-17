package com.thirteen.smp.service;


import java.util.List;
import java.util.Map;

/**
 * 主页模块业务接口
 */
public interface HomeService {

    /**
     * 获取推荐用户
     * @param count 获取推荐用户数量
     * @return 推荐用户信息列表
     */
    List<Map<String,Object>> getRecommendUser(Integer count, Integer userId);

    List<Map<String,Object>> getPostUpdate(Integer count, Integer userId);

    List<Map<String,Object>> getOnlineFriend(Integer count, Integer userId);

}
