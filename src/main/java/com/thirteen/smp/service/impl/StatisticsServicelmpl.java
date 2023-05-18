package com.thirteen.smp.service.impl;

import com.thirteen.smp.mapper.PostMapper;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServicelmpl implements StatisticsService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    PostMapper postMapper;
    @Override
    public Map<String, Object> getUserStatistics() {
        Map<String,Object> userSatistics = new LinkedHashMap<>();
        userSatistics.put("registerNum",userMapper.selectAll().size());
        userSatistics.put("onlineNum",userMapper.getOnlineUserId().size());
        return userSatistics;
    }

    @Override
    public Map<String, Object> getPostNum() {
        Map<String,Object> result= new LinkedHashMap<>();
        List<Post> posts = postMapper.selectAllPost();
        List<Post> postList = new ArrayList<>();
        Timestamp lastWeek = new Timestamp(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
        posts.forEach(post -> {
            if(post.getPostTime().compareTo(lastWeek)>0){
                postList.add(post);
            }
        });
        result.put("postNum",postList.size());
        return result;
    }
}
