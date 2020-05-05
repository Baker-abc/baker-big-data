package com.baker.learning.bigdatakafka.service;

import com.baker.learning.bigdatakafka.config.TopicConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @create: 2018-11-30 11:38
 * @Version: 1.0
 **/
@Service
@Log4j2
public class ProductKafkaMessageService {

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    ConsumerFactory consumerFactory;

    public void send1(String message) {
        log.info("send1 message：{}", message);
        kafkaTemplate.send(TopicConfig.TOPIC1, "", message);

    }

    public void send2(String message) {
        log.info("send2 message：{}", message);
        kafkaTemplate.send(TopicConfig.TOPIC2, "", message);
    }

    public void send3(String message) {
        log.info("send3 message：{}", message);
        kafkaTemplate.send(TopicConfig.TOPIC3, "", message);
    }

}
