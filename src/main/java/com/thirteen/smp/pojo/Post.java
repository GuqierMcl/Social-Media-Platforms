package com.thirteen.smp.pojo;

import java.sql.Timestamp;

/**
 * 帖子实体类，封装帖子数据
 * @author 张力文
 * @version 1.0
 * @since 1.0
 */
public class Post {

    // TODO 张力文 编写POST实体类

    private Integer postId;

    private Integer userId;

    private String content;

    private Timestamp postTime;

    private Integer likeNum;

    private String img;

    public Post() {
    }

    public Post(Integer userId, String content, String img) {
        this.userId = userId;
        this.content = content;
        this.img = img;
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
