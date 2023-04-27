package com.thirteen.smp.service;

import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.pojo.Post;

import java.util.List;

/**
 * 帖子模块业务类
 * @author 张力文
 */
public interface PostService {
    // TODO 张力文 规划业务接口
    public int savePost(Post post) throws PostNotExistException;

    public int deletePost(int postId) throws PostNotExistException;
    public List<Post> getPost_self(int userid) throws PostNotExistException;
    public List<Post> getPost_self_follow(int userid) throws PostNotExistException;
    public List<Post> queryPost(String query) throws PostNotExistException;
    public List<Post> queryPost_self(String query,int userid) throws PostNotExistException;
}
