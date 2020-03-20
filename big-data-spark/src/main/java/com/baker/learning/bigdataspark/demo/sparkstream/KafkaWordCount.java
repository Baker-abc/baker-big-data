package com.baker.learning.bigdataspark.demo.sparkstream;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.examples.streaming.StreamingExamples;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @description
 * @date 2020/3/18 16:56
 */
public class KafkaWordCount {

    private static final Pattern SPACE = Pattern.compile(" ");
    private static String appName = "spark.demo";
    private static String master = "local[*]";

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("hadoop.home.dir", "F:\\idea\\github\\baker-big-data\\big-data-spark\\");

        StreamingExamples.setStreamingLogLevels();

        SparkConf sparkConf = new SparkConf().setMaster(master).setAppName(appName);
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(2));


        Set<String> topics = Collections.singleton("spark_topic");
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", "dn1:9092,dn2:9092,dn3:9092");
        kafkaParams.put("bootstrap.servers", "dn1:9092,dn2:9092,dn3:9092");
        kafkaParams.put("group.id", "test");
        kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        kafkaParams.put("enable.auto.commit", "true");
//        kafkaParams.put("auto.commit.interval.ms", "1000");
//        kafkaParams.put("session.timeout.ms", "30000");
//        kafkaParams.put("partition.assignment.strategy", "range");

        JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(
                javaStreamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, kafkaParams));


        JavaDStream<String> lines = messages.map(new Function<ConsumerRecord<String, String>, String>() {
            @Override
            public String call(ConsumerRecord<String, String> v1) throws Exception {
                return v1.value();
            }
        });


        JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String x) {
                return Arrays.asList(SPACE.split(x)).iterator();
            }
        });

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
