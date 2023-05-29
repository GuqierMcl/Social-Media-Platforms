package com.thirteen.smp.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface SearchService {

    Map<String,Object> globalSearch(String query, HttpServletRequest request);

}
