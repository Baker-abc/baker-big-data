package com.baker.learning.bigdataspark.demo.sparksql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

/**
 * @description
 * @date 2020/3/17 16:38
 */
public class LogSparkSQL {

    private static String appName = "spark.sql.log";
    private static String master = "local";

    public static void main(String[] args) {
        JavaSparkContext javaSparkContext = null;
        try {
            javaSparkContext = new JavaSparkContext(new SparkConf().setMaster(master).setAppName(appName));
            JavaRDD<String> javaRDD = javaSparkContext.textFile("E:/sparkLog.txt");
            SQLContext sqlContext = new SQLContext(javaSparkContext);

            JavaRDD<LogInfo> logInfoJavaRDD = javaRDD.map((Function<String, LogInfo>) line -> {
                String[] str = line.split(",");
                long timeStamp = Long.valueOf(str[0]);
                String phone = str[1];
                long down = Long.valueOf(str[2]);
                long up = Long.valueOf(str[3]);
                LogInfo log = new LogInfo(timeStamp, phone, down, up);
                return log;
            });

            //将RDD转换成DataSet数据集
            Dataset<Row> df = sqlContext.createDataFrame(logInfoJavaRDD, LogInfo.class);
            //在dataset数据集上进行查询操作
            df.select("phoneNo", "down").where("up > 50").show();

            //将df注册成临时视图，这样可以用SQL表达式进行查询操作
            df.createOrReplaceTempView("log");
            Dataset<Row> seleRs = sqlContext.sql("select * from log where up > 50 and down < 70");
            seleRs.toJavaRDD().foreach(row -> System.out.println(row.get(1)));
//            seleRs.toJavaRDD().foreach(row -> System.out.println(row.toString()));


        } finally {
            if (javaSparkContext != null) {
                javaSparkContext.close();
            }
        }
    }
}
