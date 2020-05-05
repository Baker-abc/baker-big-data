package com.baker.learning.bigdatahbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class BigDataHBaseApplication {

    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir", "F:\\idea\\github\\baker-big-data\\big-data-commons\\");
        SpringApplication.run(BigDataHBaseApplication.class, args);
    }

}
