package com.thirteen.smp.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface SearchService {
    /**
     * 根据关键词搜索帖子或用户
     * @param query 查询关键词，request 请求
     * @return 搜索结果
     */
    Map<String,Object> globalSearch(String query, HttpServletRequest request);

}
