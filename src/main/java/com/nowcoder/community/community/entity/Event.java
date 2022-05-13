package com.nowcoder.community.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ZhuJiang
 * @Date: 2022/5/12 19:26
 * @Version: 1.0
 * @Description: 事件 类：主题、触发用户id、触发实体类型、触发实体id、被触发用户id，其它数据
 */
public class Event {
    private String topic;
    private int userId;
    private int entityType;
    private int entityId;
    private int entityUserId;
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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

    public int getEntityUserId() {
        return entityUserId;
    }

    public void setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(String key, Object value) {
        this.data.put(key, value);
    }
}
