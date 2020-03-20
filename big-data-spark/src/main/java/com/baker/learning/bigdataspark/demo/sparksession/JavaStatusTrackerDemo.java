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

package com.baker.learning.bigdataspark.demo.sparksession;

import org.apache.spark.SparkJobInfo;
import org.apache.spark.SparkStageInfo;
import org.apache.spark.api.java.JavaFutureAction;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.List;


public final class JavaStatusTrackerDemo {

    public static final String APP_NAME = "JavaStatusAPIDemo";
    private static String master = "local[*]";

    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSession
                .builder()
                .appName(APP_NAME)
                .master(master)
                .getOrCreate();

        final JavaSparkContext javaSparkContext = new JavaSparkContext(spark.sparkContext());

        // Example of implementing a progress reporter for a simple job.
        JavaRDD<Integer> rdd = javaSparkContext.parallelize(Arrays.asList(1, 2, 3, 4, 5), 5)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) throws Exception {
                        Thread.sleep(2000);
                        return integer;
                    }
                });
        JavaFutureAction<List<Integer>> jobFuture = rdd.collectAsync();

        while (!jobFuture.isDone()) {
            Thread.sleep(1000);  // 1 second
            List<Integer> jobIds = jobFuture.jobIds();
            if (jobIds.isEmpty()) {
                continue;
            }
            int currentJobId = jobIds.get(jobIds.size() - 1);
            SparkJobInfo jobInfo = javaSparkContext.statusTracker().getJobInfo(currentJobId);
            SparkStageInfo stageInfo = javaSparkContext.statusTracker().getStageInfo(jobInfo.stageIds()[0]);
            System.out.println("======================== " + stageInfo.numTasks() + " tasks total: " + stageInfo.numActiveTasks() +
                    " active, " + stageInfo.numCompletedTasks() + " complete");
        }

        System.out.println("Job results are: " + jobFuture.get());
        spark.stop();
    }
}
