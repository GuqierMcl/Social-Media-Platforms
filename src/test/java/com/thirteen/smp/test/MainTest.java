package com.thirteen.smp.test;

import com.thirteen.smp.mapper.CommentMapper;
import com.thirteen.smp.mapper.FollowMapper;
import com.thirteen.smp.mapper.PostMapper;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.UserService;
import com.thirteen.smp.utils.SettingUtil;
import com.thirteen.smp.utils.SqlSessionUtil;
import com.thirteen.smp.utils.AccessTokenUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.List;

/**
 * 用于测试Mapper的测试类
 *
 * @author 顾建平
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class MainTest {

    @Test
    public void testSettingUtil(){
        System.out.println(SettingUtil.getImgSavingPath());
        System.out.println(SettingUtil.getSecretKey());
    }
    @Test
    public void testFollowMapper(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        FollowMapper mapper = sqlSession.getMapper(FollowMapper.class);

        mapper.deleteFollow(8,7);

        sqlSession.commit();
    }

    @Test
    public void testPost(){

    }


    @Test
    public void testCommentMapper(){
        SqlSession sqlSession = SqlSessionUtil.openSession();
        CommentMapper mapper = sqlSession.getMapper(CommentMapper.class);

        System.out.println(mapper.selectByCommentId(2));

        System.out.println(mapper.selectByPostId(2));

        System.out.println(mapper.selectByUserId(1));

        System.out.println(mapper.selectByPreCommentId(1));

        System.out.println(mapper.selectCountByPostId(2));
    }

    @Test
    public void testPostMapper(){
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
