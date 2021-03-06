package com.nowcoder.community.community.dao;

import com.nowcoder.community.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/28 16:01
 * @Version: 1.0
 * @Description: 消息相关：
 *          1、查询当前用户会话列表（每个会话只返回一条最新的私信）
 *          2、查询当前用户会话数量
 *          3、查询会话所包含的私信列表
 *          4、查询会话所包含的私信数量
 *          5、查询未读私信数量（总的、以及各会话下的）
 *          6、增加私信
 *          7、修改会话下所有私信状态
 *              通知相关：
 *          1、查询某个主题下最新通知，（这里的 user为我，另外主题数量固定为 3 评论、赞、关注）
 *          2、查询主题所包含的通知数量
 *          3、查询未读通知数量
 *          4、查询通知所包含的消息列表
 */
@Mapper
public interface MessageMapper {

    // 因为是查询当前用户的会话列表，所以 userId 为 fromId 或 userId 为 toId 就行
    // List<Message> selectConversations(int fromId, int toId, int offset, int limit);
    List<Message> selectConversations(int userId, int offset, int limit);

    int selectConversationCount(int userId);

    List<Message> selectLetters(String conversationId, int offset, int limit);

    int selectLetterCount(String conversationId);

    int selectLetterUnreadCount(int userId, String conversationId);

    int insertMessage(Message message);

    int  updateStatus(List<Integer> ids, int status);

    /**
     *  通知，这里的 user为我
     * */
    Message selectLatestNotice(int userId, String topic);

    int selectNoticeCount(int userId, String topic);

    int selectNoticeUnreadCount(int userId, String topic);

    List<Message> selectNotices(int userId, String topic, int offset, int limit);
}
