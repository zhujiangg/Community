package com.nowcoder.community.community.controller;

import com.nowcoder.community.community.entity.Comment;
import com.nowcoder.community.community.entity.DiscussPost;
import com.nowcoder.community.community.entity.Event;
import com.nowcoder.community.community.event.EventProducer;
import com.nowcoder.community.community.service.CommentService;
import com.nowcoder.community.community.service.DiscussPostService;
import com.nowcoder.community.community.util.CommunityConstant;
import com.nowcoder.community.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/27 22:19
 * @Version: 1.0
 * @Description: 评论相关
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;
    
    @Autowired
    private DiscussPostService discussPostService;

    /**
     *  添加评论，成功重定向到当前帖子详情
     *  entityType、entityId、targetId 三个属性在 view中针对不同的位置设置
     *  content 由 post 设置
     * */
    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论事件
        Event event = new Event();
        event.setTopic(TOPIC_COMMENT);
        event.setUserId(hostHolder.getUser().getId()); // 这里 UserId即事件触发者，即谁评论的：登录的人或者评论的人
        event.setEntityId(comment.getEntityId());       // 这里 EntityId是帖子或者评论的 id
        event.setEntityType(comment.getEntityType());
        // comment中没有 EntityUserId这个属性，因此需要根据 EntityType来查询（是帖子 还是 评论）
        // 如果 EntityType为帖子，那么 EntityUserId为 评论的帖子的发布者（xx评论了你的帖子）
        // 如果 EntityType为评论，那么 EntityUserId为 回复的评论的发布者（xx回复了你的评论）
        if(comment.getEntityType() == ENTITY_TYPE_POST){
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }else if(comment.getEntityType() == ENTITY_TYPE_COMMENT){
            Comment target = commentService.findCommentsById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        event.setData("postId", discussPostId); // 不管是评论帖子还是回复评论，到时候都链接到帖子页面
        eventProducer.sendMessage(event);

        return "redirect:/discuss/detail/" + discussPostId;
    }
}
