package com.thirteen.smp.mapper;

import com.thirteen.smp.pojo.Comment;

import java.util.List;

/**
 * 用于t_comment表数据库操作的Mapper
 *
 * @version 1.0
 * @since 1.0
 */
public interface CommentMapper {

    /**
     * 通过评论ID查询评论信息
     * @param commentId 评论ID
     * @return 评论对象
     */
    Comment selectByCommentId(Integer commentId);

    /**
     * 通过帖子ID查询评论信息
     * @param postId 帖子ID
     * @return 评论对象列表
     */
    List<Comment> selectByPostId(Integer postId);

    /**
     * 通过用户ID查询评论信息
     * @param userId 用户ID
     * @return 评论对象列表
     */
    List<Comment> selectByUserId(Integer userId);

    /**
     * 通过前置评论ID查询评论信息
     * @param preCommentId 前置评论ID
     * @return 评论对象列表
     */
    List<Comment> selectByPreCommentId(Integer preCommentId);

    /**
     * 通过postId获取对应评论数量
     * @param postId 帖子ID
     * @return 评论数量
     */
    int selectCountByPostId(Integer postId);

    /**
     * 新增评论
     * @param comment 评论对象
     * @return 更新条数
     */
    int insertComment(Comment comment);

    /**
     * 通过评论ID删除评论
     * @param commentId 评论ID
     * @return 更新条数
     */
    int deleteCommentByCommentId(Integer commentId);

    /**
     * 更新评论（只更新评论内容）
     * @param comment 评论对象
     * @return 更新条数
     */
    int updateComment(Comment comment);

}
