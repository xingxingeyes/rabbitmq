package com.kai.rabbitmq.eight;

import com.kai.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;

/**
 * 死信队列实战
 */
public class Consumer02 {


    //死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();



        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer02接受到的消息：" + new String(message.getBody(), "UTF-8"));

        };
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {});


    }


}
