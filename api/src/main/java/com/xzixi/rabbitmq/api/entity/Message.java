package com.xzixi.rabbitmq.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;
    private String node;
    private T data;

}
