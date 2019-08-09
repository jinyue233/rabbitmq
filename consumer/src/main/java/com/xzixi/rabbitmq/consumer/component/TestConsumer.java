package com.xzixi.rabbitmq.consumer.component;

import com.xzixi.rabbitmq.api.Config;
import com.xzixi.rabbitmq.api.entity.Message;
import com.xzixi.rabbitmq.api.entity.User;
import com.xzixi.rabbitmq.consumer.mapper.UserMapper;
import com.xzixi.rabbitmq.zkclient.config.ZkClient;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
@RabbitListener(queues = Config.ROUTING_KEY, concurrency = "16")
public class TestConsumer {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ZkClient zkClient;

    @RabbitHandler
    public void test(Message<User> message) {
        String node = message.getNode();
        User user = message.getData();
        long now = System.currentTimeMillis();
        user.setTime(now);
        userMapper.insert(user);
        zkClient.setNodeData(node, String.valueOf(now).getBytes(Charset.forName("utf8")));
    }

}
