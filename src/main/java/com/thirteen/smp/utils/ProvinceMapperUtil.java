package com.thirteen.smp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class ProvinceMapperUtil {

    /**
     * 省份与其拼音映射枚举JSON
     */
    private static final String provinceMapStr = """
            [
                {"id":1, "name":"北京市", "spell":"beijing", "lon":116.405285, "lat":39.904989},
                {"id":2, "name":"天津市", "spell":"tianjin", "lon":117.190186, "lat":39.125596},
                {"id":3, "name":"河北省", "spell":"hebei", "lon":115.48333, "lat":38.03333},
                {"id":4, "name":"山西省", "spell":"shanxi", "lon":112.562398, "lat":37.873532},
                {"id":5, "name":"内蒙古自治区", "spell":"neimenggu", "lon":111.670801, "lat":40.818311},
                {"id":6, "name":"辽宁省", "spell":"liaoning", "lon":123.429096, "lat":41.796767},
                {"id":7, "name":"吉林省", "spell":"jilin", "lon":126.556073, "lat":43.843512},
                {"id":8, "name":"黑龙江省", "spell":"heilongjiang", "lon":126.642464, "lat":45.756967},
                {"id":9, "name":"上海市", "spell":"shanghai", "lon":121.473701, "lat":31.230416},
                {"id":10, "name":"江苏省", "spell":"jiangsu", "lon":119.412966, "lat":32.393159},
                {"id":11, "name":"浙江省", "spell":"zhejiang", "lon":120.153576, "lat":30.287459},
                {"id":12, "name":"安徽省", "spell":"anhui", "lon":117.227239, "lat":31.821858},
                {"id":13, "name":"福建省", "spell":"fujian", "lon":119.297813, "lat":26.078159},
                {"id":14, "name":"江西省", "spell":"jiangxi", "lon":115.909228, "lat":28.675697},
                {"id":15, "name":"山东省", "spell":"shandong", "lon":118.527663, "lat":36.09929},
                {"id":16, "name":"河南省", "spell":"henan", "lon":113.753394, "lat":34.765869},
                {"id":17, "name":"湖北省", "spell":"hubei", "lon":114.341862, "lat":30.546498},
                {"id":18, "name":"湖南省", "spell":"hunan", "lon":112.98381, "lat":28.112444},
                {"id":19, "name":"广东省", "spell":"guangdong", "lon":113.280637, "lat":23.125178},
                {"id":20, "name":"广西壮族自治区", "spell":"guangxi", "lon":108.327546, "lat":22.815478},
                {"id":21, "name":"海南省", "spell":"hainan", "lon":110.349229, "lat":20.017377},
                {"id":22, "name":"重庆市", "spell":"chongqing", "lon":106.504962, "lat":29.533155},
                {"id":23, "name":"四川省", "spell":"sichuan", "lon":104.065735, "lat":30.659462},
                {"id":24, "name":"贵州省", "spell":"guizhou", "lon":106.713478, "lat":26.578343},
                {"id":25, "name":"云南省", "spell":"yunnan", "lon":102.712251, "lat":25.040609},
                {"id":26, "name":"西藏自治区", "spell":"xizang", "lon":91.117212, "lat":29.646923},
                {"id":27, "name":"陕西省", "spell":"shanxi", "lon":108.95424, "lat":34.265472},
                {"id":28, "name":"甘肃省", "spell":"gansu", "lon":103.823557, "lat":36.058039},
                {"id":29, "name":"青海省", "spell":"qinghai", "lon":96.202544, "lat":35.499761},
                {"id":30, "name":"宁夏回族自治区", "spell":"ningxia", "lon":106.271942, "lat":38.468009},
                {"id":31, "name":"新疆维吾尔自治区", "spell":"xinjiang", "lon":87.627704, "lat":43.793026}
            ]
            """;

    /**
     * 省份映射集合
     */
    private static List<Map<String,Object>> provinceMapList = null;


    static {
        // 初始化省份名称映射
        ObjectMapper objectMapper = null;
        objectMapper = new ObjectMapper();
        try {
            provinceMapList = objectMapper.readValue(provinceMapStr, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将省份名称转换为拼音
     * @param provinceName 省份名称
     * @return 拼音，不存在则返回null
     */
    public static String toSpell(String provinceName){
        for (Map<String, Object> map : provinceMapList) {
            String name = (String) map.get("name");
            if (name.equals(provinceName)) {
                return (String) map.get("spell");
            }
        }
        return null;
    }

    /**
     * 将拼音转换为省份名称
     * @param spell 拼音
     * @return 省份名称，不存在则返回null
     */
    public static String toName(String spell){
        for (Map<String, Object> map : provinceMapList) {
            String provinceSpell = (String) map.get("spell");
            if (provinceSpell.equals(spell)) {
                return (String) map.get("name");
            }
        }
        return null;
    }

    /**
     * 通过省份名称获取省份映射Map
     * @param name 省份名称
     * @return 省份映射Map，不存在则返回null
     */
    public static Map<String, Object> getProvinceMapperItemByName(String name){
        for (Map<String, Object> map : provinceMapList) {
            String pName = (String) map.get("name");
            if(pName.equals(name)){
                return map;
            }
        }
        return null;
    }

    /**
     * 获取省份映射集合
     * @return 省份映射集合
     */
    public static List<Map<String, Object>> getProvinceMapList(){
        return provinceMapList;
    }
}
