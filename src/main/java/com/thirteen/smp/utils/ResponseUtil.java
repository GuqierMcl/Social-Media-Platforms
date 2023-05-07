package com.thirteen.smp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thirteen.smp.response.ResponseData;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应实体类构造工具类
 */
public class ResponseUtil {

    private static Map<Integer, String> codeMap = new HashMap<>();

    static {
        codeMap.put(1, "success");
        codeMap.put(0, "error");

        // 1xx为auth权限验证错误
        codeMap.put(101, "没有token信息（用户未登录）");
        codeMap.put(103, "无效token信息");

        // 2xx为upload文件上传错误
        codeMap.put(201, "上传文件名错误");
        codeMap.put(202, "保存文件失败");
        codeMap.put(203, "保存目录创建失败");
        codeMap.put(204, "上传文件为空");

        // 4xx为user用户错误
        codeMap.put(401, "用户不存在");
        codeMap.put(402, "用户名已存在");
        codeMap.put(403, "密码错误");

        // 5xx为数据库错误
        codeMap.put(501, "更新数据到数据库失败");

        // 6xx为帖子错误
        codeMap.put(601,"帖子不存在");
        codeMap.put(602,"评论不存在");
        codeMap.put(603,"帖子内容为空");
        codeMap.put(604,"用户未发布帖子");
        codeMap.put(605,"用户未发布帖子且关注用户未发布帖子或者未关注用户");
        codeMap.put(606,"搜索类型未指定或者搜索关键词未指出");
        codeMap.put(607,"未搜索到相关帖子");

        // 7xx为点赞错误
        codeMap.put(701,"该用户已经点赞过本帖子");
        codeMap.put(702,"该用户还没点赞本帖子");

        // 8xx为聊天错误
        codeMap.put(801,"消息不存在");

    }

    public static ResponseData getResponseData(int code, Object data) {
        return new ResponseData(code, codeMap.get(code), data);
    }

    public static ResponseData getResponseData(int code) {
        return new ResponseData(code, codeMap.get(code), null);
    }

    public static ResponseData getSuccessRes(Object data ) {
        return ResponseUtil.getResponseData(1, data);
    }
    public static ResponseData getSuccessRes() {
        return ResponseUtil.getResponseData(1, null);
    }

    public static ResponseData getErrorRes(int code) {
        return getResponseData(code);
    }

    /**
     * 将ResponseData对象转化为JSON数据
     *
     * @param responseData 响应体对象
     * @return JSON数据字符串
     */
    public static String parseAsJSON(ResponseData responseData) {
        ObjectMapper objectMapper = new ObjectMapper();
        String data = null;
        try {
            data = objectMapper.writeValueAsString(responseData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }

}
