package com.nowcoder.community.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.community.entity.Event;
import com.nowcoder.community.community.entity.Message;
import com.nowcoder.community.community.service.MessageService;
import com.nowcoder.community.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ZhuJiang
 * @Date: 2022/5/12 19:27
 * @Version: 1.0
 * @Description: 消费者 的处理（接收 event并封装入 Message中）
 */
@Component
public class EventConsumer implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW})

    public void handleMessage(ConsumerRecord record) {

        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        // 发送站内通知
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());
        if (!event.getData().isEmpty()) {
            for (String key : event.getData().keySet()) {
                content.put(key, event.getData().get(key));
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }
}
