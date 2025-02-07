package com.baker.learning.bigdatakafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建kafka的topic
 * 如果kafka不存在此topic则会自动创建，存在则不改变kafka的topic
 */
@Configuration
@EnableKafka
public class TopicConfig {


    public static final String TOPIC1 = "topic1";
    public static final String TOPIC2 = "topic2";
    public static final String TOPIC3 = "topic3";

    @Value("${mq.kaHost}")
    String kaHost;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kaHost);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        //第一个是参数是topic名字，第二个参数是分区个数，第三个是topic的复制因子个数
        //当broker个数为1个时会创建topic失败，
        //提示：replication factor: 2 larger than available brokers: 1
        //只有在集群中才能使用kafka的备份功能
        //以kafka的分区机制来说，我将其numParitions个数设置为broker个数，也就是3
        return new NewTopic(TOPIC1, 3, (short) 2);
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic(TOPIC2, 3, (short) 2);
    }

    @Bean
    public NewTopic topic3() {
        return new NewTopic(TOPIC3, 3, (short) 2);
    }

}
