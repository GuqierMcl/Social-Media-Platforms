package com.thirteen.smp.test;

import com.thirteen.smp.mapper.*;
import com.thirteen.smp.pojo.Msg;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.UserService;
import com.thirteen.smp.utils.*;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 用于测试的测试类
 *
 * @author 顾建平
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class MainTest {

    @Test
    public void batchModifyUsers(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        List<Map<String, Object>> provinceMapList = ProvinceMapperUtil.getProvinceMapList();
        Random random = new Random();
        List<User> users = mapper.selectAll();
        for (User user : users) {
            user.setUserLocation((String) provinceMapList.get(random.nextInt(provinceMapList.size())).get("name"));
            System.out.println(user);
            mapper.updateUser(user);
        }
        sqlSession.commit();
    }

    @Test
    public void testLog(){
        Logger logger = LoggerFactory.getLogger(MainTest.class);
        logger.info("Log测试");
    }

    @Test
    public void testUserStatus() {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.insertUserStatus(8, 1);
        System.out.println(mapper.selectUserStatus(8));
        mapper.updateUserStatus(8, 0);
        System.out.println(mapper.selectUserStatus(8));
        sqlSession.commit();
    }

    @Test
    public void testChatMapper() {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        ChatMapper mapper = sqlSession.getMapper(ChatMapper.class);
        mapper.insertMsg(new Msg(null, 8, 7, new Timestamp(new Date().getTime()), "你好", 0));
        sqlSession.commit();
    }

    @Test
    public void testIp() {
        System.out.println(IpAddressUtil.getIpAddress("183.221.76.134"));
        System.out.println(IpAddressUtil.getIpAddress("127.0.0.1"));
        System.out.println(IpAddressUtil.getIpAddressToMap("183.221.76.134"));
    }

    @Test
    public void testFollowMapper() {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        FollowMapper mapper = sqlSession.getMapper(FollowMapper.class);

        System.out.println(mapper.selectByUserId(2, 1));

        sqlSession.commit();
    }


    @Test
    public void testCommentMapper() {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        CommentMapper mapper = sqlSession.getMapper(CommentMapper.class);

        System.out.println(mapper.selectByCommentId(2));

        System.out.println(mapper.selectByPostId(2));

        System.out.println(mapper.selectByUserId(3));

        System.out.println(mapper.selectByPreCommentId(1));

        System.out.println(mapper.selectCountByPostId(2));
    }

    @Test
    public void testPostMapper() {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        PostMapper mapper = sqlSession.getMapper(PostMapper.class);

        List<Post> posts = mapper.selectByUserId(2);
        System.out.println(posts);
    }

    @Test
    public void testAccessToken() {
        String accessToken = AccessTokenUtil.generateAccessToken(2, "wmy", "8888888");
        System.out.println(accessToken);
        System.out.println(AccessTokenUtil.getUserId(accessToken));
        System.out.println(AccessTokenUtil.getUsername(accessToken));
        System.out.println(AccessTokenUtil.getPassword(accessToken));
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
