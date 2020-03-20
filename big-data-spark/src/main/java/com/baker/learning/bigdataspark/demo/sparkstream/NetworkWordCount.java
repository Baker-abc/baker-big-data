package com.baker.learning.bigdataspark.demo.sparkstream;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.StorageLevels;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * @description socket tool https://nmap.org/download.html
 * ncat -lk 9000
 * @date 2020/3/18 15:58
 */
public class NetworkWordCount {

    private static final Pattern SPACE = Pattern.compile(" ");
    private static final Pattern SPACES = Pattern.compile("\\s+");


    private static String appName = "spark.demo";
    private static String master = "local[*]";

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("hadoop.home.dir", "F:\\idea\\github\\baker-big-data\\big-data-spark\\");

        SparkConf sparkConf = new SparkConf().setMaster(master).setAppName(appName);
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(2));
        JavaReceiverInputDStream<String> javaReceiverInputDStream
                = javaStreamingContext.socketTextStream("127.0.0.1", 9000, StorageLevels.MEMORY_AND_DISK_SER);

        JavaDStream<String> words = javaReceiverInputDStream.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(SPACE.split(s)).iterator();
            }
        });

        words.print();

        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(
                new PairFunction<String, String, Integer>() {
                    @Override
                    public Tuple2<String, Integer> call(String s) {
                        return new Tuple2<>(s, 1);
                    }
                }).reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer i1, Integer i2) {
                return i1 + i2;
            }
        });

        wordCounts.print();

        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();


    }
}
