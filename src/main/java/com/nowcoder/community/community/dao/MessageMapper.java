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
}
