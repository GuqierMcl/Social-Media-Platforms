package com.thirteen.smp.service;

import com.thirteen.smp.pojo.History;

import java.util.List;
/**
 * 浏览记录模块业务接口
 */
public interface HistoryService {
    /**
     * 添加浏览记录
     * @param postId 帖子id userId 用户id
     * @return 返回是否添加成功
     */
    int addHistory(int postId,int userId);
    /**
     * 获取浏览记录
     * @param userId 用户id
     * @return 返回用户浏览记录对象列表
     */
    List<History> selectHistoryByUerId(int userId);
    /**
     * 删除全部浏览记录
     * @param userId 用户id
     * @return 返回是否删除成功
     */
    int deleteAllHistoryById(int userId);
    /**
     * 删除单条浏览记录
     * @param userId 用户id id 浏览记录id
     * @return 返回是否删除成功
     */
    int deleteHistoryById(int id,int userId);
}
