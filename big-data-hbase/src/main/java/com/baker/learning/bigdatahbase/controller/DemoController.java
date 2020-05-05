package com.baker.learning.bigdatahbase.controller;

import com.baker.learning.bigdatahbase.model.Demo;
import com.baker.learning.bigdatahbase.model.RespVO;
import com.baker.learning.bigdatahbase.service.DemoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @description
 * @date 2020/5/5 15:52
 */
@RestController
@RequestMapping("/demo")
@Api(value = "/demo", description = "demo")
@Slf4j
public class DemoController {

    @Autowired
    private DemoService demoService;


    @GetMapping("/get/{id}")
    public RespVO getById(@PathVariable String id) {
        try {
            List<Demo> apples = demoService.getById(new Demo(), id);
            return new RespVO().setSuccess(apples);
        } catch (Exception e) {
            log.error("异常：", e);
            return new RespVO().setFailure(e.getMessage());
        }
    }


    @GetMapping("/scan")
    public RespVO queryScan(String key, String value) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put(key, value);
            List<Demo> apples = demoService.queryScan(new Demo(), map);
            return new RespVO().setSuccess(apples);
        } catch (Exception e) {
            log.error("异常：", e);
            return new RespVO().setFailure(e.getMessage());
        }
    }

    @PostMapping("/addData")
    public RespVO addData(String rowKey, String name, String sex, String dept, String english, String math) {
        try {
            Demo demo = new Demo();
            demo.setId(rowKey);
            demo.setName(name);
            demo.setSex(sex);
            demo.setDept(dept);
            demo.setEnglish(english);
            demo.setMath(math);
            boolean apples = demoService.addData(demo);
            return new RespVO().setSuccess(demo);
        } catch (Exception e) {
            log.error("异常：", e);
            return new RespVO().setFailure(e.getMessage());
        }
    }

    @DeleteMapping("/deleteData")
    public RespVO deleteData(String rowKey) {
        try {
            demoService.deleteData(new Demo(), rowKey);
            return new RespVO().setSuccess("");
        } catch (Exception e) {
            log.error("异常：", e);
            return new RespVO().setFailure(e.getMessage());
        }
    }

}
