package com.baker.learning.bigdatahbase.hbase;

import java.lang.annotation.*;

/**
 * @description 定义model中的表名
 * @date 2020/5/5 14:52
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface HBaseTable {
    String tableName() default "";
}
