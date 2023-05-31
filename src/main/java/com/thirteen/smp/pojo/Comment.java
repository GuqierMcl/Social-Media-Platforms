package com.thirteen.smp.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * 评论实体类，用于封装评论数据
 * @author 庄可欣
 * @version 1.0
 * @since 1.0
 */
public class Comment {

    /**
     * 评论ID
     */
    private Integer commentId;

    /**
     * 评论所在帖子ID
     */
    private Integer postId;

    /**
     * 评论用户ID
     */
    private Integer userId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp commentDate;

    /**
     * 上级评论ID
     */
    private Integer preCommentId;

    public Comment() {
    }

    public Comment(Integer commentId, Integer postId, Integer userId, String commentContent, Timestamp commentDate, Integer preCommentId) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.commentContent = commentContent;
        this.commentDate = commentDate;
        this.preCommentId = preCommentId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Timestamp getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Timestamp commentDate) {
        this.commentDate = commentDate;
    }

    public Integer getPreCommentId() {
        return preCommentId;
    }

    public void setPreCommentId(Integer preCommentId) {
        this.preCommentId = preCommentId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", postId=" + postId +
                ", userId=" + userId +
                ", commentContent='" + commentContent + '\'' +
                ", commentDate=" + commentDate +
                ", preCommentId=" + preCommentId +
                '}';
    }
}
