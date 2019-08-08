package com.xzixi.rabbitmq.producer.controller;

import com.xzixi.rabbitmq.api.Config;
import com.xzixi.rabbitmq.api.entity.Message;
import com.xzixi.rabbitmq.api.entity.User;
import com.xzixi.rabbitmq.zkclient.config.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("main")
public class MainController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ZkClient zkClient;

    @GetMapping
    public String test(User user) {
        String node =  "/test/"+UUID.randomUUID().toString();
        zkClient.createNode(CreateMode.EPHEMERAL, node, "");
        System.out.println("1 : "+node);
        CountDownLatch latch = new CountDownLatch(1);
        zkClient.watchPath(node, (client, event) -> {
            System.out.println(node+": "+event.getType());
            if (TreeCacheEvent.Type.NODE_UPDATED==event.getType()) {
                String data = new String(zkClient.synNodeData(node), Charset.forName("utf8"));
                System.out.println("2 : "+node+": "+data);
                if (StringUtils.isNotBlank(data)) {
                    latch.countDown();
                }
            }
        });
        Message<User> message = new Message<>(node, user);
        rabbitTemplate.convertAndSend(Config.ROUTING_KEY, message);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String data = new String(zkClient.getNodeData(node), Charset.forName("utf8"));
        System.out.println("3 : "+node+": "+data);
        zkClient.deleteNode(node, true);
        return data;
    }

    private void watch(String node, CountDownLatch latch) {
        zkClient.watchPath(node, (client, event) -> {
            System.out.println(node+": "+event.getType());
            if (TreeCacheEvent.Type.NODE_UPDATED==event.getType()) {
                String data = new String(zkClient.synNodeData(node), Charset.forName("utf8"));
                System.out.println("2 : "+node+": "+data);
                if (StringUtils.isNotBlank(data)) {
                    latch.countDown();
                }
            }
        });
    }

}
