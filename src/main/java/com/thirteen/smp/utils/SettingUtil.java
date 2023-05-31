package com.thirteen.smp.utils;

import java.util.ResourceBundle;

/**
 * 系统全局配置工具类
 */
public class SettingUtil {

    /**
     * 全局配置对象
     */
    private static ResourceBundle bundle = ResourceBundle.getBundle("settings");

    /**
     * 获取全局配置信息
     * @param key 配置信息关键字
     * @return 对应配置值
     */
    public static String getValue(String key){
        return bundle.getString(key);
    }
}
