package com.kai.rabbitmq.one;

import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

public class Producer {

    private final static String QUEUE_NAME = "mirrior_hello";
    private final static String FED_EXCHANGE = "fed_exchange";

    public static void main(String[] args) {
        // 创建一个工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.192.100");
        factory.setUsername("admin");
        factory.setPassword("123");
        // channel实现了自动close接口自动关闭不需要显示关闭
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(FED_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.queueDeclare("node2_queue", true, false, false, null);
            channel.queueBind("node2_queue", FED_EXCHANGE, "routeKey");
            /**
             * 生成一个队列
             * 1、队列名称
             * 2、队列里面的消息是否持久化（磁盘） 默认消息存储在内存中
             * 3、该队列是否只供一个消费者进行消费 是否进行消息共享，true可以多个消费者消费 false：只能一个消费者消费
             * 4、是否自动删除 最后一个消费者端开连接 该队一句是否自动删除 true自动删除false不自动删除
             */
            Map<String, Object> arguments = new HashMap<String, Object>();
            arguments.put("x-max-priority", 10);//官方允许是0-255之间 此处设置10 允许优化级范围为0-10 不要设置过大 浪费cpu和内存
            channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);

            for (int i = 1; i < 11; i++) {
                String message = "info" + i;
                if (i == 5) {
                    AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                    channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
                } else {
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                }
            }
            //String message = "hello world";
            /**
             * 发送一个消息
             * 1、发送到那个交换机
             * 2、路由的key值是哪个 本次队列的名称
             * 3、其它参数信息
             * 4、发送消息的消息体
             */
            //channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            //System.out.println("消息发送完毕");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
