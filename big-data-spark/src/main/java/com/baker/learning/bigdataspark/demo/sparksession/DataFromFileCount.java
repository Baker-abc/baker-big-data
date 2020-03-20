package com.baker.learning.bigdataspark.demo.sparksession;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.SparkSession;

/**
 * @description
 * @date 2020/3/17 11:02
 */
public class DataFromFileCount {

    private static String appName = "spark.demo";
    private static String master = "local[*]";

    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir", "F:\\idea\\github\\baker-big-data\\big-data-spark\\");
        SparkSession sparkSession = null;

        try {

            sparkSession = SparkSession.builder().master(master).appName(appName).getOrCreate();
            /**
             * txt:
             * spark
             * sparkapsrkdesmo
             * demo
             */
            JavaRDD<String> javaRDD = sparkSession.read().textFile("E:/spark.txt").javaRDD();

            javaRDD = javaRDD.filter((Function<String, Boolean>) s -> s.contains("spark"));

            Integer result = javaRDD.map(new Function<String, Integer>() {
                @Override
                public Integer call(String s) throws Exception {
                    return s.length();
                }
            }).reduce(new Function2<Integer, Integer, Integer>() {
                @Override
                public Integer call(Integer integer, Integer integer2) throws Exception {
                    return integer + integer2;
                }
            });

            System.out.println("===================================");
            System.out.println("===================================");
            System.out.println("===================================");
            System.out.println("=================" + result + "==================");
            System.out.println("===================================");
            System.out.println("===================================");
            System.out.println("===================================");


        } finally {
            if (sparkSession != null) {
                sparkSession.stop();
            }
        }
    }
}
