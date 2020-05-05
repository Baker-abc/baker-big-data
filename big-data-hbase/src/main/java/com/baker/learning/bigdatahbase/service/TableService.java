package com.baker.learning.bigdatahbase.service;

import com.baker.learning.bigdatahbase.hbase.HBaseDaoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @description
 * @date 2020/5/5 15:13
 */
@Slf4j
@Service
public class TableService {
    @Autowired
    private HBaseDaoUtil hBaseDaoUtil;

    public void createTable(String tableName, Set<String> familyColumn) {
        hBaseDaoUtil.createTable(tableName, familyColumn);
    }

    public void dropTable(String tableName) {
        hBaseDaoUtil.dropTable(tableName);
    }

    public void addTableFamilyColumn(String tableName, String familyColumnName) {
        hBaseDaoUtil.addTableFamilyColumn(tableName, familyColumnName);
    }

    public void dropTableFamilyColumn(String tableName, String familyColumnName) {
        hBaseDaoUtil.dropTableFamilyColumn(tableName, familyColumnName);
    }

    public List<String> getFamilys(String tableName) {
        return hBaseDaoUtil.familys(tableName);
    }
}
