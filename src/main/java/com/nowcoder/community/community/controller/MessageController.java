package com.nowcoder.community.community.controller;

import com.nowcoder.community.community.entity.Message;
import com.nowcoder.community.community.entity.Page;
import com.nowcoder.community.community.entity.User;
import com.nowcoder.community.community.service.MessageService;
import com.nowcoder.community.community.service.UserService;
import com.nowcoder.community.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/28 16:48
 * @Version: 1.0
 * @Description:
 */
@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

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

        int unreadTotalCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("unreadTotalCount", unreadTotalCount);

        return "/site/letter";
    }

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

        return "/site/letter-detail";
    }

    private User getLetterTarget(String conversationId){
        String[] s = conversationId.split("_");
        int s1 = Integer.parseInt(s[0]);
        int s2 = Integer.parseInt(s[1]);
        int targetUserId = hostHolder.getUser().getId() == s1 ? s2 : s1;
        return userService.findUserById(targetUserId);
    }
}
