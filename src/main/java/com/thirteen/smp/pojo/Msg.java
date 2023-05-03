package com.thirteen.smp.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * 聊天消息封装类
 */
public class Msg {

    private Integer id;

    private Integer userId;

    private Integer toUserId;

    private Timestamp time;

    private String content;

    private Integer isRead;

    public Msg() {

    }

    public Msg(Integer id, Integer userId, Integer toUserId, Timestamp time, String content, Integer isRead) {
        this.id = id;
        this.userId = userId;
        this.toUserId = toUserId;
        this.time = time;
        this.content = content;
        this.isRead = isRead;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "id=" + id +
                ", userId=" + userId +
                ", toUserId=" + toUserId +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
