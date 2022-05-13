package com.nowcoder.community.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhuJiang
 * @Date: 2022/5/12 19:36
 * @Version: 1.0
 * @Description: 生产者的处理：把 event发出去
 */
@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(Event event) {
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
