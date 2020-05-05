package com.baker.learning.bigdatahbase.service;

import com.baker.learning.bigdatahbase.hbase.HBaseDaoUtil;
import com.baker.learning.bigdatahbase.model.Demo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @date 2020/5/5 15:14
 */
@Service
@Slf4j
public class DemoService {

    @Autowired
    private HBaseDaoUtil hBaseDaoUtil;

    public List<Demo> getById(Demo demo, String id) {
        return hBaseDaoUtil.getById(demo, id);
    }

    public <T> List<T> queryScan(Demo demo, Map<String, String> param) {
        return (List<T>) hBaseDaoUtil.queryScan(demo, param);
    }

    public boolean addData(Demo demo) {
        return hBaseDaoUtil.addData(demo);
    }

    public void deleteData(Demo demo, String rowKey) {
        hBaseDaoUtil.deleteData(demo, rowKey);
    }
}
