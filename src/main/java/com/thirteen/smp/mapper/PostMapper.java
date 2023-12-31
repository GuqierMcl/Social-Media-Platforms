package com.thirteen.smp.mapper;


import com.thirteen.smp.pojo.Post;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用于t_post表数据库操作的Mapper
 *
 * @version 1.0
 * @since 1.0
 */
public interface PostMapper {
    /**
     * 获取所有帖子
     * @return 帖子对象列表
     */
    List<Post> selectAllPost();

    /**
     * 给帖子点赞
     * @param postId 帖子ID
     * @return 是否点赞成功
     */
    Integer likePost(Integer postId);
    /**
     * 取消帖子点赞
     * @param postId 帖子ID
     * @return 是否取消点赞成功
     */
    Integer unlikePost(Integer postId);

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
     * 根据帖子内容模糊查询用户自己的帖子信息
     * @param query 搜索内容
     * @return 帖子列表
     */
    List<Post> selectByQuerySelf(@Param("query")String query, @Param("userId")int userId);

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
