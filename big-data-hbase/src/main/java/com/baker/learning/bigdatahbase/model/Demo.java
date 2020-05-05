package com.baker.learning.bigdatahbase.model;

import com.baker.learning.bigdatahbase.hbase.HBaseColumn;
import com.baker.learning.bigdatahbase.hbase.HBaseTable;
import lombok.Data;
import lombok.ToString;

/**
 * @description
 * @date 2020/5/5 15:09
 */
@HBaseTable(tableName = "student")
@Data
@ToString
public class Demo {

    @HBaseColumn(family = "rowkey", qualifier = "rowkey")
    private String id;

    @HBaseColumn(family = "info", qualifier = "name")
    private String name;

    @HBaseColumn(family = "info", qualifier = "sex")
    private String sex;

    @HBaseColumn(family = "info", qualifier = "dept")
    private String dept;

    @HBaseColumn(family = "course", qualifier = "english")
    private String english;

    @HBaseColumn(family = "course", qualifier = "math")
    private String math;
}
