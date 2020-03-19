package com.baker.learning.bigdataspark.demo.sparksql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @description
 * @date 2020/3/17 15:49
 */
@Data
@ToString
@AllArgsConstructor
public class LogInfo implements Serializable {
    private long timeStamp;
    private String phoneNo;
    private long down;
    private long up;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            String phoneNo = "1862127702" + i % 5;
            long down = (long) (Math.random() * 100);
            long up = (long) (Math.random() * 100);
            System.out.println(new Date().getTime() + "," + phoneNo + "," + down + "," + up);
            TimeUnit.SECONDS.sleep(3);
        }
    }
}
