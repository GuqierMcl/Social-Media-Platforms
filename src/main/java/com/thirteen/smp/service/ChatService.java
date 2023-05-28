package com.thirteen.smp.service;

import com.thirteen.smp.exception.UserNotExistsException;
import com.thirteen.smp.pojo.Msg;

import java.util.List;
import java.util.Map;

/**
 * 聊天模块业务接口
 */
public interface ChatService {

    /**
     * 发送消息
     * @param msg 消息对象
     * @return 执行结果
     */
    boolean sendMsg(Msg msg) throws UserNotExistsException;

    /**
     * 获取聊天记录
     * @param userId 当前用户ID
     * @param targetUserId 目标用户ID
     * @return 消息对象列表
     */
    List<Msg> getMsg(Integer userId, Integer targetUserId) throws UserNotExistsException;

    /**
     * 获取消息列表
     *
     * @param userId 当前用户ID
     * @return 用户对象列表
     */
    List<Map<String, Object>> getChatList(Integer userId);

    /**
     * 已读消息
     * @param msgId 消息ID
     * @return 执行结果
     */
    boolean setIsRead(Integer msgId);

    /**
     * 删除消息
     * @param userId 当前用户ID
     * @param targetUserId 目标用户ID
     * @return 删除消息条数
     */
    int deleteChat(Integer userId, Integer targetUserId);

    /**
     * 删除单条聊天消息
     * @param msgId 消息ID
     * @return 执行结果
     */
    int deleteChatMsg(Integer msgId);

    /**
     * 获取未读消息数
     * @param userId 当前用户ID
     * @return 消息数
     */
    int getNotReadCount(Integer userId);

}
