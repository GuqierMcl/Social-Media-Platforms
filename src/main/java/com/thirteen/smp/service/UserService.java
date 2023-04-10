package com.thirteen.smp.service;

import com.thirteen.smp.pojo.User;
import org.springframework.stereotype.Service;

/**
 * @author 顾建平
 * @version 1.0
 * @since 1.0
 */
public interface UserService {

    User getUserByUsername(String username);
}
