package com.nowcoder.community.community.service;

import com.nowcoder.community.community.dao.MessageMapper;
import com.nowcoder.community.community.entity.Message;
import com.nowcoder.community.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/28 16:43
 * @Version: 1.0
 * @Description: 消息相关：
 *          1、查询当前用户会话列表（每个会话只返回一条最新的私信）
 *          2、查询当前用户会话数量
 *          3、查询会话所包含的私信列表
 *          4、查询会话所包含的私信数量
 *          5、查询未读私信数量（总的、以及各会话下的）
 *              通知相关：
 *          1、查询某个主题下最新通知，（另外主题数量固定为 3 评论、赞、关注）
 *          2、查询主题所包含的通知数量
 *          3、查询未读通知数量
 *          4、查询通知所包含的消息列表
 */
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public int readMessage(List<Integer> ids){
        return messageMapper.updateStatus(ids, 1);
    }

    public Message findLatestNotice(int userId, String topic) {
        return messageMapper.selectLatestNotice(userId, topic);
    }

    public int findNoticeCount(int userId, String topic){
        return messageMapper.selectNoticeCount(userId, topic);
    }

    public int findNoticeUnreadCount(int userId, String topic){
        return messageMapper.selectNoticeUnreadCount(userId, topic);
    }

    public List<Message> findNotices(int userId, String topic, int offset, int limit){
        return messageMapper.selectNotices(userId, topic, offset, limit);
    }
}
