package com.xzixi.rabbitmq.consumer.component;

import com.xzixi.rabbitmq.api.Config;
import com.xzixi.rabbitmq.api.entity.Message;
import com.xzixi.rabbitmq.api.entity.User;
import com.xzixi.rabbitmq.consumer.mapper.UserMapper;
import com.xzixi.rabbitmq.zkclient.config.ZkClient;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
@RabbitListener(queues = Config.ROUTING_KEY)
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
        watch(node, () -> {
            try {
                zkClient.setNodeData(node, String.valueOf(now).getBytes(Charset.forName("utf8")));
            } catch (Exception e) {
            }
        });
    }

    private void watch(String node, Callback callback) {
        callback.execute();
        zkClient.watchPath(node, (client, event) -> {
            if (TreeCacheEvent.Type.INITIALIZED==event.getType()) {
                callback.execute();
            }
        });
    }

    private interface Callback {
        void execute();
    }

}
