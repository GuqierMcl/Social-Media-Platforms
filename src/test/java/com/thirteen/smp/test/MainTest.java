package com.thirteen.smp.test;

import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.UserService;
import com.thirteen.smp.utils.SqlSessionUtil;
import com.thirteen.smp.utils.accessTokenUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 用于测试Mapper的测试类
 *
 * @author 顾建平
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class MainTest {


    @Test
    public void testAccessToken() {
        String accessToken = accessTokenUtil.generateAccessToken(2, "wmy", "8888888");
        System.out.println(accessToken);
        System.out.println(accessTokenUtil.getUserId(accessToken));
        System.out.println(accessTokenUtil.getUsername(accessToken));
        System.out.println(accessTokenUtil.getPassword(accessToken));
    }

    @Test
    public void testUserMapper() {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectByUsername("wmy");
        UserService userService = new ClassPathXmlApplicationContext("applicationContext.xml").getBean("userServiceImpl", UserService.class);

        System.out.println(user);
        user.setNickname("表哥");
        user.setUserLang("简体中文");
        System.out.println(user);
        System.out.println(userService.updateUser(user));

        sqlSession.commit();
        sqlSession.close();
    }
}
