package com.baker.learning.bigdataapi.controller;

import com.baker.learning.bigdataapi.model.params.Message;
import com.baker.learning.bigdataapi.model.result.RespVO;
import com.baker.learning.bigdataapi.scheduled.CheckDesignScheduled;
import com.baker.learning.bigdataapi.scheduled.CheckGalleryScheduled;
import com.baker.learning.bigdatakafka.service.ProductKafkaMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @create: 2018-11-21 19:44
 * @Version: 1.0
 **/
@Log4j2
@RestController
@Api(value = "/v0.1/message", description = "上传接口")
@RequestMapping(path = "/v0.1/message", produces = {"application/json"})
public class UploadController {

    @Autowired
    ProductKafkaMessageService productKafkaMessageService;
    @Autowired
    CheckGalleryScheduled checkGalleryScheduled;
    @Autowired
    CheckDesignScheduled checkDesignScheduled;

    @ApiOperation(value = "保存信息", notes = "保存信息", httpMethod = "POST", response = Object.class)
    @RequestMapping(value = "/uploader", method = RequestMethod.POST)
    public Object saveMessage(@RequestBody Message message) throws Exception {
        RespVO respVO = new RespVO();
        String json = message.getTime() + "|" + message.getUserName() + "|" + message.getDesignName() + "|" + message.getUrl() + "|" + message.getMethod() + "|" +
                message.getParams();
        productKafkaMessageService.sendGallery(json);
        respVO.setSuccess("");
        return respVO;
    }

    @ApiOperation(value = "", notes = "", httpMethod = "GET", response = Object.class)
    @RequestMapping(value = "/gallery", method = RequestMethod.GET)
    public Object gallery() throws Exception {
        RespVO respVO = new RespVO();
        checkGalleryScheduled.update();
        respVO.setSuccess("");
        return respVO;
    }

    @ApiOperation(value = "", notes = "", httpMethod = "GET", response = Object.class)
    @RequestMapping(value = "/design", method = RequestMethod.GET)
    public Object design() throws Exception {
        RespVO respVO = new RespVO();
        checkDesignScheduled.update();
        respVO.setSuccess("");
        return respVO;
    }


}
