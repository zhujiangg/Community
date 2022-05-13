package com.nowcoder.community.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.community.entity.Message;
import com.nowcoder.community.community.entity.Page;
import com.nowcoder.community.community.entity.User;
import com.nowcoder.community.community.service.MessageService;
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
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/28 16:48
 * @Version: 1.0
 * @Description:
 */
@Controller
public class MessageController implements CommunityConstant {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    // 私信列表
    @RequestMapping(path = "/letter/list", method = RequestMethod.GET)
    public String getLetterList(Model model, Page page){
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setRows(messageService.findConversationCount(user.getId()));
        page.setPath("/letter/list");

        List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if(conversationList != null){
            for (Message message: conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount", messageService.findLetterUnreadCount(user.getId(), message.getConversationId()));
                // "target" 为发信人，我们需要取他的头像、名字等信息
                int targetUserId = user.getId()==message.getFromId()? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(targetUserId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);
        model.addAttribute("letterUnreadCount", messageService.findLetterUnreadCount(user.getId(), null));
        model.addAttribute("noticeUnreadCount", messageService.findNoticeUnreadCount(user.getId(), null));
        return "/site/letter";
    }

    // 私信详情
    @RequestMapping(path = "/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Model model, Page page){
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setRows(messageService.findLetterCount(conversationId));
        page.setPath("/letter/detail/" + conversationId);

        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if(letterList != null){
            for (Message message: letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.findUserById(message.getFromId()));

                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);

        /**
         * target 在循环外设置，因为在 "/site/letter-detail" 中需要用到的地方在循环外，不能通过 "${map.target}" 获取
         *      int targetUserId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
         *      map.put("target", userService.findUserById(targetUserId));
         * 在外面通过 conversationId 获取
         */
        model.addAttribute("target", getLetterTarget(conversationId));

        /**
         * 设置已读
         *  当登录用户是接收用户时，即我收到了消息后已读
         * */
        List<Integer> ids = new ArrayList<>();

        if (letterList != null) {
            for (Message letter : letterList) {
                if (hostHolder.getUser().getId() == letter.getToId() && letter.getStatus() == 0) {
                    ids.add(letter.getId());
                }
            }
        }
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/letter-detail";
    }

    private User getLetterTarget(String conversationId){
        String[] s = conversationId.split("_");
        int s1 = Integer.parseInt(s[0]);
        int s2 = Integer.parseInt(s[1]);
        int targetUserId = hostHolder.getUser().getId() == s1 ? s2 : s1;
        return userService.findUserById(targetUserId);
    }

    @RequestMapping(path = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(String toName, String content){
        Integer.valueOf("abc");
        User target = userService.findUserByName(toName);
        if(target == null){
            return CommunityUtil.getJsonString(1, "目标用户不存在!");
        }

        Message message = new Message();
        message.setContent(content);
        message.setCreateTime(new Date());
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if(message.getFromId() < message.getToId()){
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        }else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        messageService.addMessage(message);
        return CommunityUtil.getJsonString(0, "发送成功！");
    }

    // 通知列表
    @RequestMapping(path = "/notice/list", method = RequestMethod.GET)
    public String getNoticeList(Model model) {
        User user = hostHolder.getUser();

        // 查询评论类通知
        Message message = messageService.findLatestNotice(user.getId(), TOPIC_COMMENT);
        Map<String, Object> messageVO = new HashMap<>();
        if (message != null) {
            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_COMMENT);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_COMMENT);
            messageVO.put("unread", unread);
        }
        model.addAttribute("commentNotice", messageVO);
        System.out.println("=================================2"+message);
        System.out.println(messageVO);


        // 查询点赞类通知
        message = messageService.findLatestNotice(user.getId(), TOPIC_LIKE);
        messageVO = new HashMap<>();
        if(message != null){
            messageVO.put("message", message);
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> eventData = JSONObject.parseObject(content, HashMap.class);
            messageVO.put("user", userService.findUserById((Integer) eventData.get("userId")));
            messageVO.put("entityType", eventData.get("entityType"));
            messageVO.put("entityId", eventData.get("entityId"));
            messageVO.put("postId", eventData.get("postId"));
            messageVO.put("count", messageService.findNoticeCount(user.getId(), TOPIC_LIKE));
            messageVO.put("unread", messageService.findNoticeUnreadCount(user.getId(), TOPIC_LIKE));
        }
        model.addAttribute("likeNotice", messageVO);

        // 查询关注类通知
        message = messageService.findLatestNotice(user.getId(), TOPIC_FOLLOW);
        messageVO = new HashMap<>();
        if(message != null){
            messageVO.put("message", message);
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> eventData = JSONObject.parseObject(content, HashMap.class);
            messageVO.put("user", userService.findUserById((Integer) eventData.get("userId")));
            messageVO.put("entityType", eventData.get("entityType"));
            messageVO.put("entityId", eventData.get("entityId"));
            messageVO.put("count", messageService.findNoticeCount(user.getId(), TOPIC_FOLLOW));
            messageVO.put("unread", messageService.findNoticeUnreadCount(user.getId(), TOPIC_FOLLOW));
        }
        model.addAttribute("followNotice", messageVO);

        model.addAttribute("letterUnreadCount", messageService.findLetterUnreadCount(user.getId(), null));
        model.addAttribute("noticeUnreadCount", messageService.findNoticeUnreadCount(user.getId(), null));
        return "/site/notice";
    }

    // 通知详情
    @RequestMapping(path = "/notice/detail/{topic}")
    public String getNoticeDetail(@PathVariable("topic") String topic, Model model, Page page){
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setRows(messageService.findNoticeCount(user.getId(), topic));
        page.setPath("/notice/detail/" + topic);

        List<Message> noticeList = messageService.findNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeVOList = new ArrayList<>();
        if(noticeList != null){
            for (Message notice: noticeList) {
                Map<String, Object> map = new HashMap<>();
                map.put("notice", notice);
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> eventData = JSONObject.parseObject(content, HashMap.class);
                map.put("user", userService.findUserById((Integer) eventData.get("userId")));
                map.put("entityType", eventData.get("entityType"));
                map.put("entityId", eventData.get("entityId"));
                map.put("postId", eventData.get("postId"));
                // SYSTEM_USER，是系统通知你
                map.put("fromUser", userService.findUserById(notice.getFromId()));

                noticeVOList.add(map);
            }
        }
        model.addAttribute("notices", noticeVOList);

        /**
         * 设置已读
         * */
        List<Integer> ids = new ArrayList<>();
        if (noticeList != null) {
            for (Message letter : noticeList) {
                if (hostHolder.getUser().getId() == letter.getToId() && letter.getStatus() == 0) {
                    ids.add(letter.getId());
                }
            }
        }
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/notice-detail";
    }
}
