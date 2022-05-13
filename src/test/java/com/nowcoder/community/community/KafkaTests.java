package com.nowcoder.community.community;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

/**
 * @Author: ZhuJiang
 * @Date: 2022/5/12 10:58
 * @Version: 1.0
 * @Description: Kafka 示例
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class KafkaTests {
    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void testKafka(){
        kafkaProducer.sendMessage("test", "你好");
        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        kafkaProducer.sendMessage("test", "在吗");
    }
}

@Component
class KafkaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic, String content) {
        kafkaTemplate.send(topic, content);
    }
}

@Component
class KafkaConsumer {

    @KafkaListener(topics = {"test"})

    public void handleMessage(ConsumerRecord record) {
        System.out.println(record.value());
    }
}