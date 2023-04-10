package com.thirteen.smp.test;

import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 用于测试Mapper的测试类
 * @author 顾建平
 */
public class MapperTest {

    @Test
    public void testUserMapper(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectByUsername("user1");
        System.out.println(user);
    }
}
