package com.nowcoder.community.community.service;

import com.nowcoder.community.community.util.CommunityConstant;
import com.nowcoder.community.community.util.HostHolder;
import com.nowcoder.community.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: ZhuJiang
 * @Date: 2022/5/5 10:56
 * @Version: 1.0
 * @Description: 关注相关（我关注的实体，实体的粉丝）：
 *          1、关注
 *          2、取消关注
 *          3、查询关注实体的数量
 *          4、查询实体粉丝的数量
 *          5、查询当前用户是否已关注该实体
 *          6、查询某用户关注的人
 *          7、查询某用户的粉丝
 */
@Service
public class FollowService implements CommunityConstant {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    public void follow(int userId, int entityType, int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                operations.multi();
                redisTemplate.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
                redisTemplate.opsForZSet().add(followerKey, userId, System.currentTimeMillis());
                return operations.exec();
            }
        });
    }

    public void unfollow(int userId, int entityType, int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                operations.multi();
                redisTemplate.opsForZSet().remove(followeeKey, entityId);
                redisTemplate.opsForZSet().remove(followerKey, userId);
                return operations.exec();
            }
        });
    }

    // 关注数量：这个 user 的关注数量
    public long findFolloweeCount(int userId, int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    // 粉丝数量：这个 实体 的粉丝数量
    public long findFollowerCount(int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFolloweeKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    // 这个user 是否关注了 这个实体
    public boolean hasFollowed(int userId, int entityType, int entityId){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;
    }

    /**
     * 6、查询某用户关注的人（这里精确查询，不查询某用户关注的实体）
     *  score 默认升序，要使最新关注的在前面，因此分页时候倒序取
     *  range 左闭右闭区间
     *
     */
    public List<Map<String, Object>> findFollowees(int userId, int offset, int limit){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, ENTITY_TYPE_USER);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit - 1);
        if(targetIds == null){
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer targetId: targetIds) {
            Map<String, Object> map = new HashMap<>();
            map.put("target", userService.findUserById(targetId));
            Double score = redisTemplate.opsForZSet().score(followeeKey, targetId);
            map.put("followTime", new Date(score.longValue()));
            boolean hasFollowed = hostHolder.getUser()==null? false: hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, targetId);
            map.put("hasFollowed", hasFollowed);
            list.add(map);
        }
        return list;
    }

    public List<Map<String, Object>> findFollowers(int userId, int offset, int limit){
        String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER, userId);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);
        if(targetIds == null){
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer targetId: targetIds) {
            Map<String, Object> map = new HashMap<>();
            map.put("target", userService.findUserById(targetId));
            Double score = redisTemplate.opsForZSet().score(followerKey, targetId);
            map.put("followTime", new Date(score.longValue()));
            boolean hasFollowed = hostHolder.getUser()==null? false: hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, targetId);
            map.put("hasFollowed", hasFollowed);
            list.add(map);
        }
        return list;
    }
}
