package com.thirteen.smp.service;

import java.util.Map;

public interface StatisticsService {
    /**
     * 获取用户的相关统计信息
     * @param 无
     * @return 相关统计信息
     */
    Map<String ,Object> getUserStatistics();
    /**
     * 获取帖子相关的统计信息
     * @param 无
     * @return 相关统计信息
     */
    Map<String ,Object> getPostNum();
}
