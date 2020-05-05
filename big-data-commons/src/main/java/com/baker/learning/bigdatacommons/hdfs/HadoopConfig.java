package com.baker.learning.bigdatacommons.hdfs;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.URI;

/**
 * @description
 * @date 2020/5/5 12:46
 */
@Configuration
@Slf4j
public class HadoopConfig {

    @Value("${spring.profiles.active}")
    private String active;

    @Value("${hadoop.name-node}")
    private String nameNode;

    /**
     * 配置
     */
    public org.apache.hadoop.conf.Configuration getConfiguration() {
        //读取配置文件
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();

        conf.set("fs.defaultFS", nameNode);
        // 指定hdfs的nameservice为cluster1
        conf.set("dfs.nameservices", "cluster1");
        // cluster1下面有两个NameNode，分别是nna节点和nns节点
        conf.set("dfs.ha.namenodes.cluster1", "nna,nns");
        // nna节点下的RPC通信地址
        conf.set("dfs.namenode.rpc-address.cluster1.nna", "nna:9000");
        // nns节点下的RPC通信地址
        conf.set("dfs.namenode.rpc-address.cluster1.nns", "nns:9000");
        // 实现故障自动转移方式
        conf.set("dfs.client.failover.proxy.provider.cluster1", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        return conf;
    }

    @PostConstruct
    public void init() {
        if ("dev".equals(active)) {
            System.setProperty("hadoop.home.dir", "F:\\idea\\github\\baker-big-data\\big-data-commons\\");
        }
    }

    @Bean
    public FileSystem fs() {
        // 文件系统
        FileSystem fs = null;
        try {
            URI uri = new URI("/");
            fs = FileSystem.get(uri, this.getConfiguration(), "root");
        } catch (Exception e) {
            log.error("FileSystem初始化失败", e);
        }
        return fs;
    }

    @Bean
    @ConditionalOnBean(FileSystem.class)
    public HadoopClient hadoopClient(FileSystem fs) {
        return new HadoopClient(fs);
    }
}
