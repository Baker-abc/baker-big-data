package com.baker.learning.bigdataapi;

import com.baker.learning.bigdatacommons.hdfs.HadoopClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BigDataApiApplicationTests {

    @Autowired
    private HadoopClient hadoopClient;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testHdfs() {
        List<Map<String, String>> mapList = hadoopClient.getFileList("/");
        Optional.ofNullable(mapList).ifPresent(maps -> {
            maps.forEach(System.out::println);
        });
    }
}
