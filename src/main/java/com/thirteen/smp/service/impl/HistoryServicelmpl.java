package com.thirteen.smp.service.impl;

import com.thirteen.smp.exception.HistoryNotExistException;
import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.exception.UserNotExistsException;
import com.thirteen.smp.mapper.HistoryMapper;
import com.thirteen.smp.mapper.PostMapper;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.History;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class HistoryServicelmpl implements HistoryService {

    @Autowired
    HistoryMapper historyMapper;
    @Autowired
    PostMapper postMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional
    public int addHistory(int postId, int userId) {
        int res=0;
        Post post=postMapper.selectByPostId(postId);
        User user=userMapper.selectById(userId);
        if(post==null){
            throw new PostNotExistException("帖子不存在");
        }

        if(user==null){
            throw new UserNotExistsException("用户不存在");
        }
        if(historyMapper.jugeHistory(postId,userId)==0){
            res = historyMapper.addHistory(postId,userId,new Timestamp(new Date().getTime()));
        } else {
            res = historyMapper.updataHistoryDate(postId,userId,new Timestamp(new Date().getTime()));
        }
        return res;
    }

    @Override
    public List<History> selectHistoryByUerId(int userId) {
        User user=userMapper.selectById(userId);
        if(user==null){
            throw new UserNotExistsException("用户不存在");
        }
        List<History> historyList = historyMapper.selectHistoryByUserId(userId);
        historyList.forEach(history -> {
            Instant instant = history.getTime().toInstant();
            Instant newInstant = instant.plus(Duration.ofHours(8));
            history.setTime(Timestamp.from(newInstant));
        });
        return historyList;
    }

    @Override
    @Transactional
    public int deleteAllHistoryById(int userId) {
        User user=userMapper.selectById(userId);
        if(user==null){
            throw new UserNotExistsException("用户不存在");
        }
        int count = historyMapper.deleteAllHistoryById(userId);
        return count;
    }

    @Override
    public int deleteHistoryById(int id, int userId) {
        User user=userMapper.selectById(userId);
        if(user==null){
            throw new UserNotExistsException("用户不存在");
        }
        History history = historyMapper.selectHistoryById(id);
        if(history==null){
            throw new HistoryNotExistException("浏览记录不存在");
        }
        if(historyMapper.jugeHistory2(history.getPostId(),userId,id)==0){
            return -1;//该浏览记录不属于该用户
        }
        return historyMapper.deleteHistoryById(id);
    }
}
