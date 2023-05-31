package com.thirteen.smp.mapper;

import com.thirteen.smp.pojo.History;

import java.sql.Timestamp;
import java.util.List;

/**
 * 用于t_History表数据库操作的Mapper
 *
 * @version 1.0
 * @since 1.0
 */
public interface HistoryMapper {
    /**
     * 添加浏览记录
     * @param postId 帖子ID userId 用户Id time 浏览时间
     * @return 添加数量
     */
    int addHistory(int postId, int userId, Timestamp time);
    /**
     * 判断是否有该浏览记录
     * @param postId 帖子ID userId 用户Id
     * @return 是否存在
     */
    int judgeHistory(int postId, int userId);
    /**
     * 判断是否是用户自己的浏览记录
     * @param postId 帖子ID userId 用户Id id 浏览记录id
     * @return 是否是自己的
     */
    int judgeHistory2(int postId, int userId,int Id);
    /**
     * 更新浏览记录
     * @param postId 帖子ID userId 用户Id time 最近浏览时间
     * @return 是否更新成功
     */
    int updataHistoryDate(int postId,int userId, Timestamp time);
    /**
     * 获取用户所有的浏览记录
     * @param userId 用户Id
     * @return 浏览记录对象列表
     */
    List<History> selectHistoryByUserId(int userId);
    /**
     * 删除所有浏览记录
     * @param  userId 用户Id
     * @return 删除浏览记录的数量
     */
    int deleteAllHistoryById(int userId);
    /**
     * 删除单个浏览记录
     * @param Id 浏览记录Id
     * @return 是否删除成功
     */
    int deleteHistoryById(int Id);
    /**
     * 获取单个浏览记录
     * @param Id 浏览记录id
     * @return 浏览记录对象
     */
    History selectHistoryById(int Id);
}
