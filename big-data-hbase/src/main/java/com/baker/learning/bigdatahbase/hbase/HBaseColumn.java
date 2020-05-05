package com.baker.learning.bigdatahbase.hbase;

import java.lang.annotation.*;

/**
 * @description 用于描述表中列簇与列
 * @date 2020/5/5 14:55
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface HBaseColumn {
    String family() default "";

    String qualifier() default "";

    boolean timestamp() default false;
}
