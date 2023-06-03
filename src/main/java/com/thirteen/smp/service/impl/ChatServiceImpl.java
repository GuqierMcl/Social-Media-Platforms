package com.thirteen.smp.service.impl;

import com.thirteen.smp.exception.UserNotExistsException;
import com.thirteen.smp.mapper.ChatMapper;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.Msg;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 把一次聊天消息插入表中
     *
     * @param msg 消息对象
     * @return boolean 发送成功与否
     * @throws UserNotExistsException
     */
    @Override
    @Transactional
    public boolean sendMsg(Msg msg) throws UserNotExistsException {
        if (msg.getUserId() == null || msg.getToUserId() == null || msg.getContent().equals("")) {
            return false;
        }

        User curr = userMapper.selectById(msg.getUserId());
        User target = userMapper.selectById(msg.getToUserId());
        if (curr == null || target == null) {
            throw new UserNotExistsException("用户不存在！");
        }

        msg.setTime(new Timestamp(new Date().getTime()));
        msg.setIsRead(0);

        int i = chatMapper.insertMsg(msg);
        return i == 1;
    }

    /**
     * 获取 userId 与 targetUserId 的所有聊天记录
     *
     * @param userId       当前用户ID
     * @param targetUserId 目标用户ID
     * @return Msg 列表
     * @throws UserNotExistsException
     */
    @Override
    @Transactional
    public List<Msg> getMsg(Integer userId, Integer targetUserId) throws UserNotExistsException {
        User curr = userMapper.selectById(userId);
        User target = userMapper.selectById(targetUserId);
        if (curr == null || target == null) {
            throw new UserNotExistsException("用户不存在！");
        }
        List<Msg> msgList = chatMapper.selectById(userId, targetUserId);
        msgList.addAll(chatMapper.selectById(targetUserId, userId));
        msgList.sort(Comparator.comparing(Msg::getTime)); // 根据消息时间排序

        // 标记消息为已读
        for (Msg msg : msgList) {
            if (msg.getIsRead().equals(0) && msg.getToUserId().equals(userId)) {
                msg.setIsRead(1);
                chatMapper.updateMsg(msg);
            }
        }

        return msgList;
    }

    /**
     * 获取 userId 的所有聊天缩略
     *
     * @param userId 当前用户ID
     * @return 聊天缩略映射对象列表
     */
    @Override
    public List<Map<String, Object>> getChatList(Integer userId) {
        List<Map<String, Object>> res = new ArrayList<>();

        List<Msg> msgList1 = chatMapper.selectByUserId(userId); // 所有以 userId 作为发送者的 Msg
        List<Msg> msgList2 = chatMapper.selectByToUserId(userId); // 所有以 userId 作为接收者的 Msg

        // 去重，目的是得到所有与 userId 聊过天的 targetUserId
        LinkedHashSet<Integer> set = new LinkedHashSet<>();
        for (Msg msg : msgList1) {
            set.add(msg.getToUserId());
        }
        for (Msg msg : msgList2) {
            set.add(msg.getUserId());
        }
        List<Integer> targetUserId = new ArrayList<>(set);

        // 开始生成聊天缩略映射对象
        for (Integer id : targetUserId) {
            Msg latestMsg = ChatServiceImpl.getLatestMsg(msgList1, msgList2, id);
            User user = userMapper.selectById(id);

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("userId", user.getUserId());
            map.put("username", user.getUsername());
            map.put("nickname", user.getNickname());
            map.put("profilePic", user.getProfilePic());
            map.put("latestTime", latestMsg.getTime().toString());
            map.put("latestMsg", latestMsg.getContent());

            // 获取未读消息数
            List<Msg> msgList = chatMapper.selectById(id, userId);
            int cnt = 0;
            for (Msg msg : msgList) {
                if (msg.getIsRead() == 0){
                    cnt ++;
                }
            }
            map.put("count", cnt);
            res.add(map);
        }
        return res;
    }

    @Override
    @Transactional
    public boolean setIsRead(Integer msgId) {
        Msg msg = chatMapper.selectByMsgId(msgId);
        if (msg == null) {
            return false;
        }
        msg.setIsRead(1);
        int i = chatMapper.updateMsg(msg);
        return i == 1;
    }

    /**
     * 删除 userId 跟 targetUserId 的所有聊天记录
     *
     * @param userId       当前用户ID
     * @param targetUserId 目标用户ID
     * @return 聊天消息的数量
     * @throws UserNotExistsException
     */
    @Override
    @Transactional
    public int deleteChat(Integer userId, Integer targetUserId) throws UserNotExistsException {
        User target = userMapper.selectById(targetUserId);
        if (target == null) {
            throw new UserNotExistsException("用户不存在！");
        }

        int count = 0;
        List<Msg> list = chatMapper.selectById(userId, targetUserId);
        List<Msg> reverse_list = chatMapper.selectById(targetUserId,userId);//删除所有聊天记录
        list.addAll(reverse_list);
        for (Msg msg : list) {
            count += chatMapper.deleteMsgById(msg.getId());
        }
        return count;
    }

    @Override
    public int deleteChatMsg(Integer msgId) {
        return chatMapper.deleteMsgById(msgId);
    }

    @Override
    public int getNotReadCount(Integer userId) {
        List<Msg> msgs = chatMapper.selectByToUserId(userId);
        int cnt = 0;
        for (Msg msg : msgs) {
            if (msg.getIsRead() == 0) {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * 获取当前对象与 targetUserId 的一次最新消息
     *
     * @param list1        targetUserId 作为发送者且当前用户作为接收者的 Msg 列表
     * @param list2        targetUserId 作为接收者且当前用户作为发送者的 Msg 列表
     * @param targetUserId 当前用户的聊天对象Id
     * @return 当前用户与 targetUserId 的最新一次 Msg
     */
    private static Msg getLatestMsg(List<Msg> list1, List<Msg> list2, Integer targetUserId) {
        Msg latestMsg = null;
        for (Msg msg : list1) {
            if (msg.getToUserId().equals(targetUserId)) {
                if (latestMsg == null) {
                    latestMsg = msg;
                } else {
                    if (latestMsg.getTime().getTime() < msg.getTime().getTime()) {
                        latestMsg = msg;
                    }
                }
            }
        }
        for (Msg msg : list2) {
            if (msg.getUserId().equals(targetUserId)) {
                if (latestMsg == null) {
                    latestMsg = msg;
                } else {
                    if (latestMsg.getTime().getTime() < msg.getTime().getTime()) {
                        latestMsg = msg;
                    }
                }
            }
        }
        return latestMsg;
    }
}
