package com.baker.learning.bigdataapi.scheduled;

import com.alibaba.fastjson.JSON;
import com.baker.learning.bigdatakafka.model.DesignMessage;
import com.baker.learning.bigdatakafka.model.GalleryMessage;
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

        DesignMessage designMessage = new DesignMessage();
        designMessage.setDesignId("test");
        designMessage.setMessageType(GalleryMessage.CHANGE_EVENT);
        productKafkaMessageService.sendDesign(JSON.toJSONString(designMessage));

    }
}
