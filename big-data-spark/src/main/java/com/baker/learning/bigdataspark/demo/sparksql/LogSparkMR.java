package com.baker.learning.bigdataspark.demo.sparksql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

/**
 * @description
 * @date 2020/3/17 15:51
 */
public class LogSparkMR {

    private static String appName = "spark.log";
    private static String master = "local";

    public static void main(String[] args) {
        JavaSparkContext javaSparkContext = null;
        try {
            javaSparkContext = new JavaSparkContext(new SparkConf().setMaster(master).setAppName(appName));
            JavaRDD<String> javaRDD = javaSparkContext.textFile("E:/sparkLog.txt");

            JavaPairRDD<String, LogInfo> logPairRDD = rdd2Rddpair(javaRDD);
            JavaPairRDD<String, LogInfo> reduceByKey = aggregateByDeviceID(logPairRDD);
            reduceByKey.foreach(x -> System.out.println(x._1 + "的下行总流量：" + x._2.getDown()));
            System.out.println(reduceByKey.count());

        } finally {
            if (javaSparkContext != null) {
                javaSparkContext.close();
            }
        }
    }

    //实现strRDD到LogInfo RDD的转换   <K,V> 电话号为K，LogInfor为V
    private static JavaPairRDD<String, LogInfo> rdd2Rddpair(JavaRDD<String> accessLogRDD) {
        return accessLogRDD.mapToPair((PairFunction<String, String, LogInfo>) line -> {
            String[] logInfo = line.split(",");
            long timeStamp = Long.valueOf(logInfo[0]);
            String phone = logInfo[1];
            long down = Long.valueOf(logInfo[2]);
            long up = Long.valueOf(logInfo[2]);

            LogInfo log = new LogInfo(timeStamp, phone, down, up);
            return new Tuple2<String, LogInfo>(phone, log);
        });
    }

    //实现reduceByKey 电话号为K，将上行流量和下行流量加和
    private static JavaPairRDD<String, LogInfo> aggregateByDeviceID(JavaPairRDD<String, LogInfo> pairRDD) {
        return pairRDD.reduceByKey((Function2<LogInfo, LogInfo, LogInfo>) (v1, v2) -> {

                    //时间戳为最早的时间
                    long timeStamp = v1.getTimeStamp() < v2.getTimeStamp() ? v1.getTimeStamp() : v2.getTimeStamp();
                    //上行流量和下行流量进行add
                    long up = v1.getUp() + v2.getUp();
                    long down = v1.getDown() + v2.getDown();
                    String phone = v1.getPhoneNo();
                    return new LogInfo(timeStamp, phone, up, down);
                }
        );
    }
}
