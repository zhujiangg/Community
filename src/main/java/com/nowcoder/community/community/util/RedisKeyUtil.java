package com.nowcoder.community.community.util;

import org.springframework.stereotype.Component;

/**
 * @Author: ZhuJiang
 * @Date: 2022/5/4 10:32
 * @Version: 1.0
 * @Description: 生成 redis中的 key：
 *      某个实体的赞  like:entity:entityType:entityId  ： set(userId)
 *      某个用户的赞  like:user:userId  ： int
 *      某个用户关注的实体   followee:userId:entityType  ： zset(entityId,now)
 *      某个实体拥有的粉丝   follower:entityType:entityId ： zset(userId,now)
 *      验证码     kaptcha:owner  ：  value(kaptcha)
 *      凭证      ticket:ticket  ： value(loginTicket)
 *      用户信息    user:userId  ：  value(User)
 *
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";

    public static String getEntityLikeKey(int entityType, int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    public static String getFolloweeKey(int userId, int entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    public static String getFollowerKey(int entityType, int entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    public static String getTicketKey(String ticket){
        return PREFIX_TICKET + SPLIT + ticket;
    }

    public static String getUserKey(int userId){
        return PREFIX_USER + SPLIT + userId;
    }
}
