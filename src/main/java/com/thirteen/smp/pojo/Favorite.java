package com.thirteen.smp.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class Favorite {
    private Integer id;
    private Integer postId;
    private Integer userId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
