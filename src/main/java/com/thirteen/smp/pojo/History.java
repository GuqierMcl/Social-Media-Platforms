package com.thirteen.smp.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class History {
    private int id;
    private int postId;
    private int userId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public History(int id, int postId, int userId, Timestamp time) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.time = time;
    }
}
