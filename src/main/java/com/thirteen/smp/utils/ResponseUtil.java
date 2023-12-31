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

    /**
     * 静态结果码枚举Map
     */
    private static Map<Integer, String> codeMap = new HashMap<>();

    /* 结果码枚举初始化 */
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
        codeMap.put(405, "用户名或昵称不合法");
        codeMap.put(406, "敏感词库IO异常");
        codeMap.put(407, "body参数为空");
        codeMap.put(408, "验证码错误");

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
        codeMap.put(609,"帖子内容不合法");
        codeMap.put(608,"评论不存在");

        // 7xx为点赞错误
        codeMap.put(701,"该用户已经点赞过本帖子");
        codeMap.put(702,"该用户还没点赞本帖子");
        codeMap.put(703,"该用户已经点赞过本评论");
        codeMap.put(704,"评论内容不合法");

        // 8xx为聊天错误
        codeMap.put(801,"消息不存在");

        // 9xx为浏览记录错误
        codeMap.put(901,"浏览记录条数为零");
        codeMap.put(902,"该浏览记录不属于本用户");
        codeMap.put(903,"浏览记录不存在");
        codeMap.put(904,"type输入错误");
    }

    /**
     * 获取响应对象
     * @param code 结果码
     * @param data 数据对象
     * @return 响应对象
     */
    public static ResponseData getResponseData(int code, Object data) {
        return new ResponseData(code, codeMap.get(code), data);
    }

    /**
     * 获取响应对象
     * @param code 结果码
     * @return 响应对象
     */
    public static ResponseData getResponseData(int code) {
        return new ResponseData(code, codeMap.get(code), null);
    }

    /**
     * 获取成功响应对象
     * @param data 响应数据
     * @return 响应对象
     */
    public static ResponseData getSuccessResponse(Object data ) {
        return ResponseUtil.getResponseData(1, data);
    }

    /**
     * 获取成功响应对象
     * @return 响应对象
     */
    public static ResponseData getSuccessResponse() {
        return ResponseUtil.getResponseData(1, null);
    }

    /**
     * 获取错误响应对象
     * @param code 结果码
     * @return 响应对象
     */
    public static ResponseData getErrorResponse(int code) {
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
