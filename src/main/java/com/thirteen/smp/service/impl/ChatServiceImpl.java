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

        int i = chatMapper.insetMsg(msg);
        return i == 1;
    }

    @Override
    public List<Msg> getMsg(Integer userId, Integer targetUserId) throws UserNotExistsException {
        User curr = userMapper.selectById(userId);
        User target = userMapper.selectById(targetUserId);
        if (curr == null || target == null) {
            throw new UserNotExistsException("用户不存在！");
        }
        List<Msg> msgList = chatMapper.selectById(userId, targetUserId);
        msgList.addAll(chatMapper.selectById(targetUserId, userId));
        msgList.sort(Comparator.comparing(Msg::getTime)); // 根据消息时间排序
        return msgList;
    }

    @Override
    public List<Map<String, Object>> getChatList(Integer userId) {
        List<Map<String, Object>> res = new ArrayList<>();

        List<Msg> msgList1 = chatMapper.selectByUserId(userId);
        List<Msg> msgList2 = chatMapper.selectByToUserId(userId);

        // 去重
        LinkedHashSet<Integer> set = new LinkedHashSet<>();
        for (Msg msg : msgList1) {
            set.add(msg.getToUserId());
        }
        for (Msg msg : msgList2) {
            set.add(msg.getUserId());
        }
        List<Integer> targetUserId = new ArrayList<>(set);

        // 匹配用户信息
        for (Integer id : targetUserId) {
            Msg latestMsg = ChatServiceImpl.getLatestMsg(msgList1, msgList2, id);
            User user = userMapper.selectById(id);

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("userId", latestMsg.getToUserId());
            map.put("username", user.getUsername());
            map.put("nickname", user.getNickname());
            map.put("proFilePic", user.getProfilePic());
            map.put("latestTime", latestMsg.getTime().toString());
            map.put("latestMsg", latestMsg.getContent());

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

    @Override
    @Transactional
    public int deleteChat(Integer userId, Integer targetUserId) throws UserNotExistsException {
        User target = userMapper.selectById(targetUserId);
        if (target == null) {
            throw new UserNotExistsException("用户不存在！");
        }

        int count = 0;
        List<Msg> list = chatMapper.selectById(userId, targetUserId);
        for (Msg msg : list) {
            count += chatMapper.deleteMsgById(msg.getId());
        }
        return count;
    }

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
