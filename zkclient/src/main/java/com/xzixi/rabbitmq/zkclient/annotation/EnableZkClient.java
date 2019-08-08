package com.xzixi.rabbitmq.zkclient.annotation;

import com.xzixi.rabbitmq.zkclient.config.ZkClientConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ZkClientConfig.class)
public @interface EnableZkClient {
}
