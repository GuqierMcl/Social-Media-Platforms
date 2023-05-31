package com.thirteen.smp.service;

import com.thirteen.smp.exception.CommentNotExistException;
import com.thirteen.smp.exception.PostNotExistException;
import com.thirteen.smp.pojo.Comment;

import java.util.List;
import java.util.Map;

/**
 * 评论模块业务接口
 * @author 庄可欣
 */
public interface CommentService {


    /**
     * 获取帖子对应的评论数量
     *
     * @param postId 帖子ID
     * @return 评论数量
     */
    int getCount(Integer postId) throws PostNotExistException;

    /**
     * 获取帖子对应的评论
     *
     * @param postId 帖子ID
     * @return 评论列表
     */
    List<Map<String, Object>> getComments(Integer postId, Integer userId) throws PostNotExistException;

    /**
     * 发布评论
     *
     * @param comment 评论对象
     * @return 处理结果
     */
    boolean publishComment(Comment comment) throws PostNotExistException, CommentNotExistException;

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @return 处理结果
     */
    boolean deleteComment(Integer commentId);
}
