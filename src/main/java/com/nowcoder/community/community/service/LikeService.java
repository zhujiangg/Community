package com.nowcoder.community.community.service;

import com.nowcoder.community.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhuJiang
 * @Date: 2022/5/4 10:38
 * @Version: 1.0
 * @Description: 点赞相关：
 *          1、点一次点赞，点两次取消
 *          2、查询某实体（1 帖子、2 评论）点赞的数量
 *          3、查询某人对某实体的点赞状态（0 未点、1 点赞）
 *          4、查询某个用户获得的赞
 */
@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void like(int userId, int entityType, int entityId, int entityUserId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                Boolean member = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
                operations.multi();
                if(member){
                    redisTemplate.opsForSet().remove(entityLikeKey, userId);
                    redisTemplate.opsForValue().decrement(userLikeKey);
                }else {
                    redisTemplate.opsForSet().add(entityLikeKey, userId);
                    redisTemplate.opsForValue().increment(userLikeKey);
                }
                return operations.exec();
            }
        });
    }

    public long findEntityLikeCount(int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    // 0 未点赞，1 已点赞
    public int findEntityLikeStatus(int userId, int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        if(redisTemplate.opsForSet().isMember(entityLikeKey, userId)){
            return 1;
        }else {
            return 0;
        }
    }

    public int findUserLikeCount(int entityUserId){
        String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        if(count == null){
            return 0;
        }else {
            return count.intValue();
        }
    }
}
