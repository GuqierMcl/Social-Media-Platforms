package com.thirteen.smp.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * 帖子实体类，封装帖子数据
 * @author 张力文
 * @version 1.0
 * @since 1.0
 */
public class Post {

    /**
     * 帖子ID
     */
    private Integer postId;

    /**
     * 发帖用户ID
     */
    private Integer userId;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 发帖时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp postTime;

    /**
     * 点赞数
     */
    private Integer likeNum;

    /**
     * 帖子图片
     */
    private String img;

    public Post() {
    }

    public Post(Integer userId, String content, String img) {
        this.userId = userId;
        this.content = content;
        this.img = img;
        this.likeNum=0;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", postTime=" + postTime +
                ", likeNum=" + likeNum +
                ", img='" + img + '\'' +
                '}';
    }
}
