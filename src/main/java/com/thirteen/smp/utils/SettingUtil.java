package com.thirteen.smp.utils;

import java.util.ResourceBundle;

public class SettingUtil {

    private static ResourceBundle bundle = ResourceBundle.getBundle("settings");

    public static String getImgSavingPath(){
        return bundle.getString("imgSavingPath");
    }

    public static String getSecretKey(){
        return bundle.getString("secretKey");
    }
}
