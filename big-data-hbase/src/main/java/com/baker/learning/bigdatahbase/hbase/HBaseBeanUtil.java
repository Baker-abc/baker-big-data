package com.baker.learning.bigdatahbase.hbase;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @description HBase对象和Obj转换
 * @date 2020/5/5 14:59
 */
@Slf4j
public class HBaseBeanUtil {

    /**
     * JavaBean转换为Put
     *
     * @param obj
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> Put beanToPut(T obj) throws Exception {
        Put put = new Put(Bytes.toBytes(parseObjId(obj)));
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
            if (StringUtils.isBlank(family) || StringUtils.isBlank(qualifier)) {
                continue;
            }
            Object fieldObj = field.get(obj);
            if (fieldObj.getClass().isArray()) {
                log.error("nonsupport");
            }
            if ("rowkey".equalsIgnoreCase(qualifier) || "rowkey".equalsIgnoreCase(family)) {
                continue;
            }
            if (field.get(obj) != null || StringUtils.isNotBlank(field.get(obj).toString())) {
                put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(field.get(obj).toString()));
            }
        }
        return put;
    }

    /**
     * 获取Bean中的id,作为Rowkey
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String parseObjId(T obj) {
        Class<?> clazz = obj.getClass();
        try {
            Field field = clazz.getDeclaredField("id");
            field.setAccessible(true);
            Object object = field.get(obj);
            return object.toString();
        } catch (NoSuchFieldException | IllegalAccessException | SecurityException | IllegalArgumentException e) {
            log.error("", e);
        }
        return "";
    }

    /**
     * HBase result转换为bean
     *
     * @param result
     * @param obj
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T resultToBean(Result result, T obj) throws Exception {
        if (result == null) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(HBaseColumn.class)) {
                continue;
            }
            HBaseColumn orm = field.getAnnotation(HBaseColumn.class);
            String family = orm.family();
            String qualifier = orm.qualifier();
            boolean timeStamp = orm.timestamp();
            if (StringUtils.isBlank(family) || StringUtils.isBlank(qualifier)) {
                continue;
            }
            String fieldName = field.getName();
            String value = "";
            if ("rowkey".equalsIgnoreCase(family)) {
                value = new String(result.getRow());
            } else {
                value = getResultValueByType(result, family, qualifier, timeStamp);
            }
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String setMethodName = "set" + firstLetter + fieldName.substring(1);
            Method setMethod = clazz.getMethod(setMethodName, new Class[]{field.getType()});
            setMethod.invoke(obj, new Object[]{value});
        }
        return obj;
    }


    private static String getResultValueByType(Result result, String family, String qualifier, boolean timeStamp) {
        if (!timeStamp) {
            return new String(result.getValue(Bytes.toBytes(family), Bytes.toBytes(qualifier)));
        }
        List<Cell> cells = result.getColumnCells(Bytes.toBytes(family), Bytes.toBytes(qualifier));
        if (cells.size() == 1) {
            Cell cell = cells.get(0);
            return cell.getTimestamp() + "";
        }
        return "";
    }
}
