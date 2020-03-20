/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baker.learning.bigdataspark.demo.sparkstream;


import com.google.common.collect.Lists;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.examples.streaming.StreamingExamples;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

public final class JavaQueueStream {

    private static final Pattern SPACE = Pattern.compile(" ");
    private static final Pattern SPACES = Pattern.compile("\\s+");

    private static String appName = "spark.demo";
    private static String master = "local[*]";

    public static void main(String[] args) throws Exception {

        System.setProperty("hadoop.home.dir", "F:\\idea\\github\\baker-big-data\\big-data-spark\\");

        StreamingExamples.setStreamingLogLevels();

        SparkConf sparkConf = new SparkConf().setMaster(master).setAppName(appName);
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(2));


        Queue<JavaRDD<Integer>> rddQueue = new LinkedList<>();

        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        for (int i = 0; i < 30; i++) {
            rddQueue.add(javaStreamingContext.sparkContext().parallelize(list));
        }

        // Create the QueueInputDStream and use it do some processing
        JavaDStream<Integer> inputStream = javaStreamingContext.queueStream(rddQueue);
        JavaPairDStream<Integer, Integer> mappedStream = inputStream.mapToPair(
                new PairFunction<Integer, Integer, Integer>() {
                    @Override
                    public Tuple2<Integer, Integer> call(Integer i) {
                        return new Tuple2<>(i % 10, 1);
                    }
                });
        JavaPairDStream<Integer, Integer> reducedStream = mappedStream.reduceByKey(
                new Function2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer i1, Integer i2) {
                        return i1 + i2;
                    }
                });

        reducedStream.print();
        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }
}
