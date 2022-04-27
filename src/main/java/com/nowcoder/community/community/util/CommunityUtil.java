package com.nowcoder.community.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.UUID;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/19 19:46
 * @Version: 1.0
 * @Description: 工具类（经常用到的方法设置成 static，省去了每次 new对象的内存空间）
 *                 1、生成随机字符串
 *                 2、将密码 MD5加密
 *                 3、构造 Json 对象并转化成 String
 */
public class CommunityUtil {

    // 生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replace("-", ""); //删除 UUID中的 ”-“
    }

    /**
     * key = password + salt
     * StringUtils.isBlank() 判断是否为null
     * DigestUtils.md5DigestAsHex() 加密
     */
    public static String md5(String key){
        if(StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * 构造 Json 对象并转化成 String
     * */
    public static String getJsonString(int code, String msg, Map<String, Object> map){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if(map != null){
            for (String key: map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }
    public static String getJsonString(int code, String msg){
        return getJsonString(code, msg, null);
    }
    public static String getJsonString(int code){
        return getJsonString(code, null, null);
    }
}
