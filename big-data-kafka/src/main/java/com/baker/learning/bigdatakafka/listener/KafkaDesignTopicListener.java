package com.baker.learning.bigdatakafka.listener;

import com.baker.learning.bigdatakafka.config.TopicConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class KafkaDesignTopicListener {


    //配置topic和分区
    @KafkaListener(
            id = "${jc.consumer.designGroupId}",
            topicPartitions = {
                    @TopicPartition(topic = TopicConfig.TOPIC_KAFKA_DESIGN_CHANGE,
                            partitions = "#{'${jc.consumer.partitions}'.split(',')}")
            }
    )
    public void receive(@Payload List<String> messages,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets, Acknowledgment ack) {

        for (int i = 0; i < messages.size(); i++) {
            String msg = "message='" + messages.get(i) + "' with partition-offset='" + partitions.get(i) + "-" + offsets.get(i) + "'";

            log.info("receive messages {}", msg);
        }
        ack.acknowledge();
        log.info("all batch messages {} consumed", messages.size());
    }

}
