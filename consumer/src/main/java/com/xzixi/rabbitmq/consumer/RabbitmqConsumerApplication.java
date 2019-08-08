package com.xzixi.rabbitmq.consumer;

import com.xzixi.rabbitmq.zkclient.annotation.EnableZkClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZkClient
@MapperScan(basePackages = "com.xzixi.rabbitmq.consumer.mapper")
public class RabbitmqConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqConsumerApplication.class, args);
    }

}
