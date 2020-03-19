package com.baker.learning.bigdataapi.scheduled;

import com.alibaba.fastjson.JSON;
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
public class CheckGalleryScheduled {

    @Autowired
    ProductKafkaMessageService productKafkaMessageService;

    public void update() {
        GalleryMessage galleryMessage = new GalleryMessage();
        galleryMessage.setChange(true);
        galleryMessage.setMessageType(GalleryMessage.CHANGE_EVENT);
        productKafkaMessageService.sendGallery(JSON.toJSONString(galleryMessage));
    }
}
