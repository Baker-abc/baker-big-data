package com.baker.learning.bigdataapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan("com.baker.learning")
public class BigDataApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BigDataApiApplication.class, args);
    }

}
