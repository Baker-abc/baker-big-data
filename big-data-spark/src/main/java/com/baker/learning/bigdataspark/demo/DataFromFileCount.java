package com.baker.learning.bigdataspark.demo;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

/**
 * @description
 * @date 2020/3/17 11:02
 */
public class DataFromFileCount {

    private static String appName = "spark.demo";
    private static String master = "local[*]";

    public static void main(String[] args) {
        JavaSparkContext javaSparkContext = null;

        try {
            javaSparkContext = new JavaSparkContext(new SparkConf().setAppName(appName).setMaster(master));
            /**
             * txt:
             * spark
             * sparkapsrkdesmo
             * demo
             */
            JavaRDD<String> javaRDD = javaSparkContext.textFile("E:/spark.txt");

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
            if (javaSparkContext != null) {
                javaSparkContext.close();
            }
        }
    }
}
