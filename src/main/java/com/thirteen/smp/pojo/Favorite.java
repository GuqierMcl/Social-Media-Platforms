package com.thirteen.smp.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
/**
 * 收藏夹实体类，封装收藏夹数据
 * @author 庄可欣
 * @version 1.0
 * @since 1.0
 */
public class Favorite {

    /**
     * 收藏ID
     */
    private Integer id;

    /**
     * 收藏帖子ID
     */
    private Integer postId;

    /**
     * 收藏用户ID
     */
    private Integer userId;

    /**
     * 收藏时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp time;

    public Favorite() {
    }

    public Favorite(Integer id, Integer postId, Integer userId, Timestamp time) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "id=" + id +
                ", postId=" + postId +
                ", userId=" + userId +
                ", time=" + time +
                '}';
    }
}
