package com.thirteen.smp.service;

import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.pojo.Post;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * 帖子模块业务类
 * @author 张力文
 */
public interface PostService {
    // TODO 张力文 规划业务接口

    /**
     * 获取所有帖子并进行数据包装
     * @param userId 当前用户id
     * @return 经过包装之后的所有帖子列表
     */
    List<Map<String,Object>> getAllPost(int userId);
    /**
     * 获取单个帖子
     * @param postId 帖子id
     * @return 帖子信息
     */
    Map<String,Object> getPostById(int userid, int postId) throws PostNotExistException;
    /**
     * 发布帖子
     * @param post 帖子对象
     * @return 是否保存成功
     */
    int savePost(Post post) throws PostNotExistException;
    /**
     * 删除帖子
     * @param postId 帖子id
     * @return 是否删除成功
     */
    int deletePost(int postId) throws PostNotExistException;
    /**
     * 获得本用户发布的帖子
     * @param userId 用户id
     * @return 帖子列表
     */
    List<Map<String,Object>> getPostSelf(int userId, int nowUserId) throws PostNotExistException;
    /**
     * 获得本用户和关注用户发布的帖子
     * @param userId 用户id
     * @return 帖子列表
     */
    List<Map<String,Object>> getPostSelfFollow(int userId) throws PostNotExistException;
}
