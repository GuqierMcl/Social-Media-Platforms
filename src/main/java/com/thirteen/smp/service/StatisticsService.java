package com.thirteen.smp.service;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    /**
     * 获取用户的相关统计信息
     *
     * @return 相关统计信息
     */
    List<Map<String, Object>> getUserStatistics();
    /**
     * 获取帖子相关的统计信息
     * @return 相关统计信息
     */
    List<Integer> getPostNum();

    /**
     * 获取用户热度排行榜
     * @param count 获取数量
     * @return 用户排行榜
     */
    List<Map<String, Object>> getHotUserList(Integer count, Integer userId) throws Exception;

    /**
     * 获取帖子热度排行榜
     * @param count 获取数量
     * @return 帖子排行榜
     */
    List<Map<String, Object>> getHotPostList(Integer count) throws Exception;

    /**
     * 获取已生成用户热度排行榜
     * @param count 获取数量
     * @param userId 当前用户ID
     * @return 排行榜列表对象
     * @throws Exception 异常
     */
    List<Map<String, Object>> getGeneratedHotUserList(Integer count, Integer userId) throws Exception;

    /**
     * 获取已生成帖子热度排行榜
     * @param count 获取数量
     * @return 排行榜列表对象
     * @throws Exception 异常
     */
    List<Map<String, Object>> getGeneratedHotPostList(Integer count) throws Exception;
}
