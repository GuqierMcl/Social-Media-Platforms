package com.thirteen.smp.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 敏感词检测工具类
 */
public class BannedWordUtil {


    /**
     * 是否是敏感词
     * @param keyWord 关键词
     * @return 是否是敏感词
     * @throws IOException IO异常
     */
    public static boolean isBannedWord(String keyWord) throws IOException {

        // 敏感词监测

        // 读取敏感词库
        ClassPathResource classPathResource = new ClassPathResource("pubBannedWords.txt");
        List<String> bannedWords = new ArrayList<>(); // 用于存储读取得到敏感词
        BufferedReader reader = new BufferedReader(new FileReader(classPathResource.getFile()));// 通过ClassPathResource进一步得到BufferedReader，用户字符读取
        // 保存敏感词到list
        for (; ; ) {
            String str = reader.readLine();
            if (str == null) break;
//            System.out.println(str);
            bannedWords.add(str);
        }
        // 查询关键词是否包含敏感词
        for (String bannedWord : bannedWords) {
            if (keyWord.contains(bannedWord)) {
                return true;
            }
        }
        return false;
    }
}
