package com.thirteen.smp.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * 历史记录实体封装类
 * @author 张力文
 * @version 1.0
 * @since 1.0
 */
public class History {

    /**
     * 历史记录ID
     */
    private int id;

    /**
     * 历史记录帖子ID
     */
    private int postId;

    /**
     * 历史记录用户ID
     */
    private int userId;

    /**
     * 历史记录添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
