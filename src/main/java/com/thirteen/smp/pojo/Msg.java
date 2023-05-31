package com.thirteen.smp.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * 聊天消息封装类
 * @version 1.0
 * @since 1.0
 */
public class Msg {

    /**
     * 消息ID
     */
    private Integer id;

    /**
     * 消息发送用户ID
     */
    private Integer userId;

    /**
     * 消息接收用户ID
     */
    private Integer toUserId;

    /**
     * 消息发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp time;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息是否已读状态
     */
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
