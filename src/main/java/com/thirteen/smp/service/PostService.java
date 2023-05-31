package com.thirteen.smp.service;

import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.pojo.Post;

import java.util.List;
import java.util.Map;

/**
 * 帖子模块业务类
 * @author 张力文
 */
public interface PostService {
    // TODO 张力文 规划业务接口
    /**
     * 发布帖子
     * @param post 帖子对象
     * @return 是否保存成功
     */
    public int savePost(Post post) throws PostNotExistException;
    /**
     * 删除帖子
     * @param postId 帖子id
     * @return 是否删除成功
     */
    public int deletePost(int postId) throws PostNotExistException;
    /**
     * 获得本用户发布的帖子
     * @param userid 用户id
     * @return 帖子列表
     */
    public List<Map<String,Object>> getPostSelf(int userid) throws PostNotExistException;
    /**
     * 获得本用户和关注用户发布的帖子
     * @param userid 用户id
     * @return 帖子列表
     */
    public List<Map<String,Object>> getPostSelfFollow(int userid) throws PostNotExistException;
    /**
     * 搜索帖子
     * @param query 搜索关键词
     * @return 帖子列表
     */
    public List<Post> queryPost(String query) throws PostNotExistException;
    /**
     * 搜索特定用户的帖子
     * @param userid 用户id
     * @return 帖子列表
     */
    public List<Post> queryPostSelf(String query,int userid) throws PostNotExistException;
}
