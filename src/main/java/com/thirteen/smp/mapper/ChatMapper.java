package com.thirteen.smp.mapper;

import com.thirteen.smp.pojo.Msg;

import java.util.List;

/**
 * 用于t_chat表的数据库操作mapper
 *
 * @version 1.0
 * @since 1.0
 */
public interface ChatMapper {

    /**
     * 通过用户ID查询聊天消息
     * @param userId 用户ID
     * @return 消息对象集合
     */
    List<Msg> selectByUserId(Integer userId);

    /**
     * 通过目标用户ID查询聊天消息
     * @param toUserId 目标用户ID
     * @return 消息对象集合
     */
    List<Msg> selectByToUserId(Integer toUserId);

    /**
     * 通过ID查询聊天消息
     * @param userId 用户ID
     * @param toUserId 目标用户ID
     * @return 消息对象集合
     */
    List<Msg> selectById(Integer userId, Integer toUserId);

    /**
     * 通过消息ID查询消息
     * @param msgId 消息ID
     * @return 消息对象
     */
    Msg selectByMsgId(Integer msgId);

    /**
     * 插入消息
     * @param msg 消息对象
     * @return 执行结果
     */
    int insertMsg(Msg msg);

    /**
     * 更新消息
     * @param msg 消息对象
     * @return 执行结果
     */
    int updateMsg(Msg msg);

    /**
     * 删除消息
     * @param msgId 消息ID
     * @return 执行结果
     */
    int deleteMsgById(Integer msgId);
}
