package com.kai.rabbitmq.five;

import com.kai.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 消息接收
 */
public class ReceiveLogs02 {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //声明一个队列 临时队列
        /**
         * 生成一个临时队列、队列的名称是随机的
         * 当消费者断开与队列连接的时候 队列就自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机与队列
         */
        channel.queueBind(queueName, EXCHANGE_NAME,"");
        System.out.println("等待接受消息，把接受到的消息打印在屏幕上......");
        //接收消息
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("ReceiveLogs02控制台打印接受到的消息："+new String(message.getBody(),"UTF-8"));
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});


    }

}
