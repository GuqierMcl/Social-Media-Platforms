package com.thirteen.smp.mapper;

import com.thirteen.smp.pojo.History;

import java.sql.Timestamp;
import java.util.List;

public interface HistoryMapper {
    int addHistory(int postId, int userId, Timestamp time);
    int jugeHistory(int postId, int userId);
    int jugeHistory2(int postId, int userId,int id);
    int updataHistoryDate(int postId,int userId, Timestamp time);
    List<History> selectHistoryByUserId(int userId);

    int deleteAllHistoryById(int userId);
    int deleteHistoryById(int id);
    History selectHistoryById(int id);
}
