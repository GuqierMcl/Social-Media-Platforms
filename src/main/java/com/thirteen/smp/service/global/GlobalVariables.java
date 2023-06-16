package com.thirteen.smp.service.global;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    public static Integer recommendPostNum = 5;
    /**
     * 推荐帖子源于附近省份数量
     */
    public static Integer NearestProvinceNum = 5;

}
