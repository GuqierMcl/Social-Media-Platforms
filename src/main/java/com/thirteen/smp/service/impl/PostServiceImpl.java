package com.thirteen.smp.service.impl;

import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.mapper.FollowMapper;
import com.thirteen.smp.mapper.PostMapper;
import com.thirteen.smp.mapper.UserMapper;
import com.thirteen.smp.pojo.Post;
import com.thirteen.smp.pojo.User;
import com.thirteen.smp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    // TODO 张力文 实现帖子业务接口
    @Autowired
    private PostMapper postMapper;// 使用Spring自动注入工具类

    @Autowired
    private FollowMapper followMapper;// 使用Spring自动注入工具类

    @Override
    @Transactional // 启用事务
    public int savePost(Post post) throws PostNotExistException {
        if(post.getContent()==null||post.getContent().equals("")){
            return -1; //表示帖子内容为空
        }
        post.setPostTime(new Timestamp(new Date().getTime()));
        int count =  postMapper.insertPost(post);

        // 自定义的异常必须继承RuntimeException才会被事务管理器回滚
        if(count==0) {//为0表示添加事务出错
            throw new PostNotExistException("添加帖子失败");
        }
        else {
            return count;
        }

    }

    @Override
    @Transactional // 启用事务
    public int deletePost(int postId) throws PostNotExistException {
        int count=0;
        count = postMapper.deletePostByPostId(postId);
        if(count==0){
            throw new PostNotExistException("删除帖子失败");
        } else {
            return count;
        }
    }

    @Override
    public List<Post> getPost_self(int userid) throws PostNotExistException {
        List<Post> posts = null;
        posts = postMapper.selectByUserId(userid);
        if(posts==null||posts.size()==0){
            throw new PostNotExistException("该用户没有发布帖子");
        } else{
            return posts;
        }

    }

    @Override
    public List<Post> getPost_self_follow(int userid) throws PostNotExistException {
        List<Post> posts = null;
        posts = postMapper.selectByUserId(userid);
        List<User> Follows = followMapper.selectByFollowerUserId(userid);
        List<Post> finalPosts = posts;
        Follows.forEach(follow ->{
            finalPosts.addAll(postMapper.selectByUserId(follow.getUserId()));
        });
        if(finalPosts.size()==0){
            throw new PostNotExistException("该用户未发布帖子且关注用户未发布帖子或者未关注其他用户");
        } else{
            return finalPosts;
        }
    }

    @Override
    public List<Post> queryPost(String query) throws PostNotExistException {
        List<Post> posts = null;
        posts = postMapper.selectByQuery(query);
        if(posts.size()==0){
            throw new PostNotExistException("未搜索到相关帖子");
        } else{
            return posts;
        }
    }
    @Override
    public List<Post> queryPost_self(String query,int userid) throws PostNotExistException {
        List<Post> posts = null;
        posts = postMapper.selectByQuery_self(query,userid);
        if(posts.size()==0){
            throw new PostNotExistException("未搜索到相关帖子");
        } else{
            return posts;
        }
    }
}
