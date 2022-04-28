package com.nowcoder.community.community.entity;

import java.util.Date;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/27 10:08
 * @Version: 1.0
 * @Description: 帖子实体类
 * +-------------+-----------+------+-----+---------+----------------+
 * | Field       | Type      | Null | Key | Default | Extra          |
 * +-------------+-----------+------+-----+---------+----------------+
 * | id          | int(11)   | NO   | PRI | NULL    | auto_increment |
 * | user_id     | int(11)   | YES  | MUL | NULL    |                |
 * | entity_type | int(11)   | YES  |     | NULL    |                | 评论目标的类别：'1-帖子; 2-用户;'
 * | entity_id   | int(11)   | YES  | MUL | NULL    |                | 评论目标的 id（评论的帖子 id、回复的评论id）
 * | target_id   | int(11)   | YES  |     | NULL    |                | 指向性评论（回复 回复的id）
 * | content     | text      | YES  |     | NULL    |                |
 * | status      | int(11)   | YES  |     | NULL    |                | 默认设置为 0
 * | create_time | timestamp | YES  |     | NULL    |                |
 * +-------------+-----------+------+-----+---------+----------------+
 */
public class Comment {
    private int id;
    private int userId;
    private int entityType;
    private int entityId;
    private int targetId;
    private String content;
    private int status;
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
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
        return "Comment{" + "id=" + id + ", userId=" + userId + ", entityType=" + entityType + ", entityId=" + entityId + ", targetId=" + targetId + ", content='" + content + '\'' + ", status=" + status + ", createTime=" + createTime + '}';
    }
}
