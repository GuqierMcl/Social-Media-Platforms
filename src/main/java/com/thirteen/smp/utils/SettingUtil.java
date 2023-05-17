package com.thirteen.smp.utils;

import java.util.ResourceBundle;

public class SettingUtil {

    private static ResourceBundle bundle = ResourceBundle.getBundle("settings");

    public static String getValue(String key){
        return bundle.getString(key);
    }
}
