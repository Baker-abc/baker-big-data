package com.baker.learning.bigdataapi.scheduled;

import com.alibaba.fastjson.JSON;
import com.baker.learning.bigdatakafka.model.Message1;
import com.baker.learning.bigdatakafka.service.ProductKafkaMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Slf4j
public class CheckGalleryScheduled {

    @Autowired
    ProductKafkaMessageService productKafkaMessageService;

    public void update() {
        Message1 message1 = new Message1();
        message1.setChange(true);
        message1.setMessageType(Message1.CHANGE_EVENT);
        productKafkaMessageService.send1(JSON.toJSONString(message1));
    }
}
