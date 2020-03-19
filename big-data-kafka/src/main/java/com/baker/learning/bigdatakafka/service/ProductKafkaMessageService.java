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

    public void sendGallery(String message) {
        log.info("sendGallery message：{}", message);
        kafkaTemplate.send(TopicConfig.TOPIC_KAFKA_GALLERY_CHANGE, "", message);

    }

    public void sendDesign(String message) {
        log.info("sendDesign message：{}", message);
        kafkaTemplate.send(TopicConfig.TOPIC_KAFKA_DESIGN_CHANGE, "", message);
    }

    public void sendDesignExportRooms(String message) {
        log.info("sendDesignExportRooms message：{}", message);
        kafkaTemplate.send(TopicConfig.TOPIC_EXPORT_ROOMS, "", message);
    }

}
