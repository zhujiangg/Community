package com.nowcoder.community.community.entity;

import java.util.Date;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/28 14:37
 * @Version: 1.0
 * @Description: 消息实体类
 * +-----------------+-------------+------+-----+---------+----------------+
 * | Field           | Type        | Null | Key | Default | Extra          |
 * +-----------------+-------------+------+-----+---------+----------------+
 * | id              | int(11)     | NO   | PRI | NULL    | auto_increment |
 * | from_id         | int(11)     | YES  | MUL | NULL    |                |    ‘1为系统消息'
 * | to_id           | int(11)     | YES  | MUL | NULL    |                |
 * | conversation_id | varchar(45) | NO   | MUL | NULL    |                |    ‘小的 id在前面’
 * | content         | text        | YES  |     | NULL    |                |
 * | status          | int(11)     | YES  |     | NULL    |                |    '0-未读;1-已读;2-删除;',
 * | create_time     | timestamp   | YES  |     | NULL    |                |
 * +-----------------+-------------+------+-----+---------+----------------+
 */
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Message{" + "id=" + id + ", fromId=" + fromId + ", toId=" + toId + ", conversationId='" + conversationId + '\'' + ", content='" + content + '\'' + ", status=" + status + ", createTime=" + createTime + '}';
    }
}
