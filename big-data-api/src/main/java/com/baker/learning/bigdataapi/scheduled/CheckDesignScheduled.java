package com.baker.learning.bigdataapi.scheduled;

import com.alibaba.fastjson.JSON;
import com.baker.learning.bigdatakafka.model.Message1;
import com.baker.learning.bigdatakafka.model.Message2;
import com.baker.learning.bigdatakafka.service.ProductKafkaMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Slf4j
public class CheckDesignScheduled {

    @Autowired
    ProductKafkaMessageService productKafkaMessageService;

    public void update() {

        Message2 message2 = new Message2();
        message2.setDesignId("test");
        message2.setMessageType(Message1.CHANGE_EVENT);
        productKafkaMessageService.send2(JSON.toJSONString(message2));

    }
}
