package com.baker.learning.bigdatahbase.hbase;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @description
 * @date 2020/5/5 15:02
 */
@Component
@Slf4j
public class HBaseDaoUtil {

    // 关闭连接
//    public static void close() {
//        if (HBConnectionFactory.connection != null) {
//            try {
//                HBConnectionFactory.connection.close();
//            } catch (IOException e) {
//                log.error("", e);
//            }
//        }
//    }


    public void createTable(String tableName, Set<String> familyColumn) {
        TableName tn = TableName.valueOf(tableName);
        try {
            Admin admin = HBConnectionFactory.connection.getAdmin();
            HTableDescriptor htd = new HTableDescriptor(tn);
            for (String fc : familyColumn) {
                HColumnDescriptor hcd = new HColumnDescriptor(fc);
                htd.addFamily(hcd);
            }
            admin.createTable(htd);
        } catch (IOException e) {
            log.error("创建" + tableName + "表失败！", e);
        }
    }


    public void dropTable(String tableName) {
        TableName tn = TableName.valueOf(tableName);
        try {
            Admin admin = HBConnectionFactory.connection.getAdmin();
            admin.disableTable(tn);
            admin.deleteTable(tn);
        } catch (IOException e) {
            log.error("删除" + tableName + "表失败！");
        }
    }


    public void addTableFamilyColumn(String tableName, String familyColumnName) {
        TableName tn = TableName.valueOf(tableName);
        try {
            Admin admin = HBConnectionFactory.connection.getAdmin();
            HColumnDescriptor hcd = new HColumnDescriptor(familyColumnName);
            admin.addColumn(tn, hcd);
        } catch (IOException e) {
            log.error("修改" + tableName + "表，增加列簇" + familyColumnName + "失败！");
        }
    }


    public void dropTableFamilyColumn(String tableName, String familyColumnName) {
        TableName tn = TableName.valueOf(tableName);
        try {
            Admin admin = HBConnectionFactory.connection.getAdmin();
            byte[] b = familyColumnName.getBytes("UTF-8");
            admin.deleteColumn(tn, b);
        } catch (IOException e) {
            log.error("修改" + tableName + "表，删除列簇" + familyColumnName + "失败！");
        }
    }


    public List<String> familys(String tableName) {
        try (Table table = HBConnectionFactory.connection.getTable(TableName.valueOf(tableName));) {
            List<String> columns = new ArrayList<String>();
            if (table == null) {
                return columns;
            }
            HTableDescriptor tableDescriptor = table.getTableDescriptor();
            HColumnDescriptor[] columnDescriptors = tableDescriptor.getColumnFamilies();
            for (HColumnDescriptor columnDescriptor : columnDescriptors) {
                String columnName = columnDescriptor.getNameAsString();
                columns.add(columnName);
            }
            return columns;
        } catch (Exception e) {
            log.error("查询列簇名称失败！", e);
        }
        return new ArrayList<String>();
    }

    public <T> List<T> getById(T obj, String rowkey) {
        List<T> objs = new ArrayList<T>();
        String tableName = getORMTable(obj);
        if (StringUtils.isBlank(tableName)) {
            return objs;
        }
        try {
            Table table = HBConnectionFactory.connection.getTable(TableName.valueOf(tableName));
            Admin admin = HBConnectionFactory.connection.getAdmin();
            if (!admin.isTableAvailable(TableName.valueOf(tableName))) {
                return objs;
            }
            List<Result> results = getResults(tableName, rowkey);
            if (results.isEmpty()) {
                return objs;
            }
            for (int i = 0; i < results.size(); i++) {
                T bean = null;
                Result result = results.get(i);
                if (result == null || result.isEmpty()) {
                    continue;
                }
                try {
                    bean = HBaseBeanUtil.resultToBean(result, obj);
                } catch (Exception e) {
                    log.error("查询异常！", e);
                }
                objs.add(bean);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return objs;
    }

    // 获取tableName
    private String getORMTable(Object obj) {
        HBaseTable table = obj.getClass().getAnnotation(HBaseTable.class);
        return table.tableName();
    }

    // 获取查询结果
    private List<Result> getResults(String tableName, String... rowkeys) {
        List<Result> resultList = new ArrayList<Result>();
        List<Get> gets = new ArrayList<Get>();
        for (String rowkey : rowkeys) {
            if (StringUtils.isBlank(rowkey)) {
                continue;
            }
            Get get = new Get(Bytes.toBytes(rowkey));
            gets.add(get);
        }
        try (Table table = HBConnectionFactory.connection.getTable(TableName.valueOf(tableName));) {
            Result[] results = table.get(gets);
            Collections.addAll(resultList, results);
            return resultList;
        } catch (Exception e) {
            log.error("", e);
            return resultList;
        }
    }

    public <T> List<T> queryScan(T obj, Map<String, String> param) {
        List<T> objs = new ArrayList<T>();
        String tableName = getORMTable(obj);
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        try {
            Table table = HBConnectionFactory.connection.getTable(TableName.valueOf(tableName));
            Admin admin = HBConnectionFactory.connection.getAdmin();
            if (!admin.isTableAvailable(TableName.valueOf(tableName))) {
                return objs;
            }
            Scan scan = new Scan();
            for (Map.Entry<String, String> entry : param.entrySet()) {
                Class<?> clazz = obj.getClass();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (!field.isAnnotationPresent(HBaseColumn.class)) {
                        continue;
                    }
                    field.setAccessible(true);
                    HBaseColumn orm = field.getAnnotation(HBaseColumn.class);
                    String family = orm.family();
                    String qualifier = orm.qualifier();
                    if (qualifier.equals(entry.getKey())) {
                        Filter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(entry.getKey()), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(entry.getValue()));
                        scan.setFilter(filter);
                    }
                }
            }
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                T beanClone = (T) BeanUtils.cloneBean(HBaseBeanUtil.resultToBean(result, obj));
                objs.add(beanClone);
            }
        } catch (Exception e) {
            log.error("查询失败！", e);
            return null;
        }
        return objs;
    }

    // 新增
    private boolean putData(List<Put> puts, String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return false;
        }
        try {
            Table table = HBConnectionFactory.connection.getTable(TableName.valueOf(tableName));
            Admin admin = HBConnectionFactory.connection.getAdmin();
            table.put(puts);
            return true;
        } catch (IOException e) {
            log.error("", e);
            return false;
        }
    }

    public <T> boolean addData(T... objs) {
        List<Put> puts = new ArrayList<>();
        String tableName = "";
        try {
            Admin admin = HBConnectionFactory.connection.getAdmin();
            for (Object obj : objs) {
                if (obj == null) {
                    continue;
                }
                tableName = getORMTable(obj);
                // 表不存在，先获取family创建表
                if (!admin.isTableAvailable(TableName.valueOf(tableName))) {
                    // 获取family, 创建表
                    Class<?> clazz = obj.getClass();
                    Field[] fields = clazz.getDeclaredFields();
                    Set<String> set = new HashSet<>(10);
                    for (int i = 0; i < fields.length; i++) {
                        if (!fields[i].isAnnotationPresent(HBaseColumn.class)) {
                            continue;
                        }
                        fields[i].setAccessible(true);
                        HBaseColumn orm = fields[i].getAnnotation(HBaseColumn.class);
                        String family = orm.family();
                        if ("rowkey".equalsIgnoreCase(family)) {
                            continue;
                        }
                        set.add(family);
                    }
                    // 创建表
                    createTable(tableName, set);
                }
                Put put = HBaseBeanUtil.beanToPut(obj);
                puts.add(put);
            }
        } catch (Exception e) {
            log.error("添加数据异常！", e);
        }
        return putData(puts, tableName);
    }

    public <T> void deleteData(T obj, String... rowkeys) {
        String tableName = "";
        tableName = getORMTable(obj);
        if (StringUtils.isBlank(tableName)) {
            return;
        }
        List<Delete> deletes = new ArrayList<Delete>();
        for (String rowkey : rowkeys) {
            if (StringUtils.isBlank(rowkey)) {
                continue;
            }
            deletes.add(new Delete(Bytes.toBytes(rowkey)));
        }
        delete(deletes, tableName);
    }

    private void delete(List<Delete> deletes, String tableName) {
        try (Table table = HBConnectionFactory.connection.getTable(TableName.valueOf(tableName));) {
            if (StringUtils.isBlank(tableName)) {
                log.info("tableName为空！");
                return;
            }
            table.delete(deletes);
        } catch (IOException e) {
            log.error("删除失败！", e);
        }
    }
}
