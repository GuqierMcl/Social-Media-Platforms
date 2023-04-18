package com.thirteen.smp.mapper;


import com.thirteen.smp.pojo.Post;

import java.util.List;

/**
 * 用于t_post表数据库操作的Mapper
 */
public interface PostMapper {

    /**
     * 根据帖子ID查询帖子信息
     * @param postId 帖子ID
     * @return 帖子对象
     */
    Post selectByPostId(Integer postId);

    /**
     * 根据用户ID查询帖子信息
     * @param userId 用户ID
     * @return 帖子对象列表
     */
    List<Post> selectByUserId(Integer userId);

    /**
     * 根据帖子内容模糊查询帖子信息
     * @param query 搜索内容
     * @return 帖子列表
     */
    List<Post> selectByQuery(String query);

    /**
     * 新增帖子
     * @param post 帖子对象
     * @return 更新条数
     */
    int insertPost(Post post);

    /**
     * 根据帖子ID删除帖子
     * @param postId 帖子ID
     * @return 更新条数
     */
    int deletePostByPostId(Integer postId);

    /**
     * 更新帖子信息（只更新帖子内容、图片）
     * @param post 帖子对象
     * @return 更新条数
     */
    int updatePost(Post post);

}
