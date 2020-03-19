package com.baker.learning.bigdataspark.demo;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description
 * @date 2020/3/13 14:55
 */
public class DataCount {

    private static String appName = "spark.demo";
    private static String master = "local[*]";

    public static void main(String[] args) {
        JavaSparkContext javaSparkContext = null;

        try {
            SparkConf sparkConf = new SparkConf().setAppName(appName).setMaster(master);
            javaSparkContext = new JavaSparkContext(sparkConf);

            //构建数据源
            List<Integer> data = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

            JavaRDD<Integer> rdd = javaSparkContext.parallelize(data);

            Integer result = rdd
                    .map((Function<Integer, Integer>) integer -> integer)
                    .reduce((Function2<Integer, Integer, Integer>) (integer, integer2) -> integer + integer2);

            System.out.println(result);
        } finally {
            if (javaSparkContext != null) {
                javaSparkContext.close();
            }
        }


    }
}
