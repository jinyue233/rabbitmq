package com.xzixi.rabbitmq.zkclient.config;

import com.xzixi.rabbitmq.zkclient.properties.ZookeeperProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZkClientConfig {

    @Bean(initMethod = "init", destroyMethod = "stop")
    public ZkClient zkClient(ZookeeperProperties zookeeperProperties) {
        return new ZkClient(zookeeperProperties);
    }

}
