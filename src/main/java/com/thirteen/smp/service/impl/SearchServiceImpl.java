package com.thirteen.smp.service.impl;

import com.thirteen.smp.exception.HistoryNotExistException;
import com.thirteen.smp.mapper.PostMapper;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    PostMapper postMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public Map<String, Object> globalSearch(String query) {
        Map<String,Object> datas = new LinkedHashMap<>();
        List<Post> posts = postMapper.selectByQuery(query);
        List<User> users = userMapper.selectByQuery(query);
        datas.put("posts",posts);
        datas.put("users",users);
        return datas;
    }
}
