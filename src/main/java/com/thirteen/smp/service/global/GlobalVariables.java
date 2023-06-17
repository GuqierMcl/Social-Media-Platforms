package com.thirteen.smp.service.global;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 全局变量类
 */
@Component
public class GlobalVariables {

    /**
     * 已生成验证码
     */
    public static List<String> GENERATED_CODE = new ArrayList<>();

    /**
     * 推荐帖子数量
     */
    public static Integer RECOMMEND_POST_NUM = 5;

    /**
     * 推荐帖子源于附近省份数量
     */
    public static Integer NEAREST_PROVINCE_NUM = 5;

    /**
     * 已生成的用户热度列表
     */
    public static Map<String, Object> HOT_USER_LIST;

    /**
     * 已生成的帖子热度列表
     */
    public static Map<String, Object> HOT_POST_LIST;

}
