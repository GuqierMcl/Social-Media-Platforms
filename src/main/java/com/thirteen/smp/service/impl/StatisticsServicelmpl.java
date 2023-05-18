package com.thirteen.smp.service.impl;

import com.thirteen.smp.mapper.PostMapper;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
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
        userSatistics.put("registerNum",userMapper.getRegisterNum());
        userSatistics.put("onlineNum",userMapper.getOnlineNum());
        return userSatistics;
    }

    @Override
    public Map<String, Object> getPostNum() {
        Map<String,Object> result= new LinkedHashMap<>();
        result.put("postNum",postMapper.getPostByTime());
        return result;
    }
}
