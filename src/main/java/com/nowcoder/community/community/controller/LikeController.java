package com.nowcoder.community.community.controller;

import com.nowcoder.community.community.entity.Comment;
import com.nowcoder.community.community.entity.DiscussPost;
import com.nowcoder.community.community.entity.Event;
import com.nowcoder.community.community.entity.User;
import com.nowcoder.community.community.event.EventProducer;
import com.nowcoder.community.community.service.LikeService;
import com.nowcoder.community.community.util.CommunityConstant;
import com.nowcoder.community.community.util.CommunityUtil;
import com.nowcoder.community.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ZhuJiang
 * @Date: 2022/5/4 10:54
 * @Version: 1.0
 * @Description: 点赞相关：异步
 */
@Controller
public class LikeController implements CommunityConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId, int postId){
        User user = hostHolder.getUser();
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        // 触发点赞事件
        // 因为点一下点赞，再点一下取消赞，因此只有在赞的时候才触发事件：0 未点赞，1 已点赞
        if(likeStatus == 1){
            Event event = new Event();
            event.setTopic(TOPIC_LIKE);
            event.setUserId(hostHolder.getUser().getId());
            event.setEntityId(entityId);
            event.setEntityType(entityType);
            event.setEntityUserId(entityUserId);
            event.setData("postId", postId); // 不管是评论帖子还是回复评论，到时候都链接到帖子页面
            eventProducer.sendMessage(event);
        }

        return CommunityUtil.getJsonString(0, null, map);
    }
}
