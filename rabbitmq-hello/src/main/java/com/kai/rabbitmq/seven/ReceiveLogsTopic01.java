package com.kai.rabbitmq.seven;

import com.kai.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 声明主题交换机及相关队列
 */
public class ReceiveLogsTopic01 {

    public static final String EXCHANGE_NAME = "topic_log";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        //声明一个队列
        String queueName = "Q1";
        channel.queueDeclare(queueName, false, false, false,null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        System.out.println("等待接受消息....");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsTopic01控制台打印接受到的消息：" + new String(message.getBody(), "UTF-8"));
            System.out.println("接收队列：" + queueName +"绑定键："+message.getEnvelope().getRoutingKey());
        };

        channel.basicConsume(queueName,true, deliverCallback, consumerTag -> {});


    }


}
