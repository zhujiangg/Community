package com.nowcoder.community.community.controller;

import com.nowcoder.community.community.annotation.LoginRequired;
import com.nowcoder.community.community.entity.Page;
import com.nowcoder.community.community.entity.User;
import com.nowcoder.community.community.service.FollowService;
import com.nowcoder.community.community.service.UserService;
import com.nowcoder.community.community.util.CommunityConstant;
import com.nowcoder.community.community.util.CommunityUtil;
import com.nowcoder.community.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Author: ZhuJiang
 * @Date: 2022/5/5 11:06
 * @Version: 1.0
 * @Description: 关注、取消关注、关注列表、粉丝列表
 */
@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    /**
     *  关注
     * */
    @LoginRequired // 定义注解：登录才需访问
    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId){
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);
        return CommunityUtil.getJsonString(0, "已关注！");
    }

    /**
     *  取消关注
     * */
    @LoginRequired
    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId){
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(), entityType, entityId);
        return CommunityUtil.getJsonString(0, "已取消关注！");
    }

    /**
     *  关注列表
     *  这里的 user 是 所访问的人
     * */
    @RequestMapping(path =  "/followees/{userId}" , method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId") int userId, Model model, Page page){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("用户不存在！");
        }
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId, ENTITY_TYPE_USER));

        List<Map<String, Object>> followees = followService.findFollowees(userId, page.getOffset(), page.getLimit());
        model.addAttribute("followees", followees);

        return "/site/followee";
    }

    @RequestMapping(path =  "/followers/{userId}" , method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") int userId, Model model, Page page){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("用户不存在！");
        }
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setRows((int)followService.findFollowerCount(ENTITY_TYPE_USER, userId));
        page.setPath("/followers" + userId);
        List<Map<String, Object>> followers = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        model.addAttribute("followers", followers);

        return "/site/follower";
    }
}
