package com.baker.learning.bigdataspark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BigDataSparkApplication {

    public static void main(String[] args) {
        System.setProperty("spark.executor.memory", "512m");
        System.setProperty("hadoop.home.dir", "F:\\idea\\github\\baker-big-data\\big-data-spark\\");
        SpringApplication.run(BigDataSparkApplication.class, args);
    }

}
