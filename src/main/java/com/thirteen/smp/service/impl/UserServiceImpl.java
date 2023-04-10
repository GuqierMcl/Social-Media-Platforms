package com.thirteen.smp.service.impl;

import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByUsername(String username){
        return userMapper.selectByUsername(username);
    }

}
