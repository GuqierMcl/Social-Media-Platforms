package com.thirteen.smp.utils;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class IpAddressUtil {
    public static String getIpAddress(String ip) {
//        if ("127.0.0.1".equals(ip) || ip.startsWith("192.168")) {
//            return "局域网 ip";
//        }

        // 1、创建 searcher 对象
        Searcher searcher = null;
        File file = null;
        String dbPath = null;
        try {
            file = ResourceUtils.getFile("classpath:ipdb/ip2region.xdb");
            dbPath = file.getPath();
            searcher = Searcher.newWithFileOnly(dbPath);
        } catch (IOException e) {
            System.out.printf("failed to create searcher with `%s`: %s\n", dbPath, e);
            return null;
        }

        // 2、查询
        String region = null;
        try {
            long sTime = System.nanoTime();
            region = searcher.search(ip);
            long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
            System.out.printf("{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);
        } catch (Exception e) {
            System.out.printf("failed to search(%s): %s\n", ip, e);
        }

        // 3、关闭资源

        try {
            searcher.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        // 备注：并发使用，每个线程需要创建一个独立的 searcher 对象单独使用。
        return region;
    }

    public static Map<String,String> getIpAddressToMap(String ip){
        String address = getIpAddress(ip);
        if (address != null) {
            String[] strings = address.split("\\|");
            Map<String,String> ipMap = new LinkedHashMap<>();
            ipMap.put("country",strings[0]);
            ipMap.put("region",strings[1]);
            ipMap.put("province",strings[2]);
            ipMap.put("city",strings[3]);
            ipMap.put("operator",strings[4]);
            return ipMap;
        }
        return null;
    }
}
