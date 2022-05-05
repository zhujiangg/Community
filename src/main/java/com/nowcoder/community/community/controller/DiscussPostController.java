package com.nowcoder.community.community.controller;


import com.nowcoder.community.community.entity.Comment;
import com.nowcoder.community.community.entity.DiscussPost;
import com.nowcoder.community.community.entity.Page;
import com.nowcoder.community.community.entity.User;

import com.nowcoder.community.community.service.CommentService;
import com.nowcoder.community.community.service.DiscussPostService;
import com.nowcoder.community.community.service.LikeService;
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


import java.util.*;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/26 10:16
 * @Version: 1.0
 * @Description: 帖子相关
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    /**
     *  发布帖子
     * */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content){
        User user = hostHolder.getUser();
        if(user == null){
            CommunityUtil.getJsonString(403, "你还没登录噢！");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);
        return CommunityUtil.getJsonString(0, "发布成功!");
    }

    /**
     *  获取帖子详情：发布人、内容、评论、回复…
     *
     * */
    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Page page, Model model){
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount", likeCount);
        int likeStatus = hostHolder.getUser() == null? 0 :likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus", likeStatus);

        // 评论的分页信息
        page.setRows(post.getCommentCount());
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);

        // 评论: 给帖子的评论
        // 回复: 给评论的评论

        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // commentVoList
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if(commentList != null){
            for (Comment comment: commentList) {
                User commentUser = userService.findUserById(comment.getUserId());// 当前评论的人：掉脑袋切切
                Map<String, Object> commentVo = new HashMap<>();
                commentVo.put("comment", comment);
                commentVo.put("user", commentUser); // 评论作者
                commentVo.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId()));
                likeStatus = hostHolder.getUser() == null? 0: likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus", likeStatus); // 若用户未登录，赞不了

                // replyVoList
                List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if(replyList != null){
                    for (Comment reply: replyList) {
                        User replyUser = userService.findUserById(reply.getUserId());// 评论下当前回复的人：寒江雪
                        Map<String, Object> replyVo = new HashMap<>();
                        replyVo.put("reply", reply);
                        replyVo.put("user", replyUser);
                        // 获取 回复 评论中回复 的用户
                        User target = reply.getTargetId() == 0? null: userService.findUserById(reply.getTargetId()); // 对 评论下当前回复 进行回复的人：Sissi
                        replyVo.put("target", target);
                        replyVo.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId()));
                        likeStatus = hostHolder.getUser() == null? 0: likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeStatus", likeStatus); // 若用户未登录，赞不了
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量 replyCount
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }
}
