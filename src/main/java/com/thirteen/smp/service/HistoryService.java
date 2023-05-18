package com.thirteen.smp.service;

import com.thirteen.smp.pojo.History;

import java.util.List;

public interface HistoryService {
    int addHistory(int postId,int userId);

    List<History> selectHistoryByUerId(int userId);

    int deleteAllHistoryById(int userId);

    int deleteHistoryById(int id,int userId);
}
