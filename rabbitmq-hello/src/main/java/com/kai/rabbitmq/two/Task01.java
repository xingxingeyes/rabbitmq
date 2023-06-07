package com.kai.rabbitmq.two;

import com.kai.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * 生产者发送大量的消息
 */
public class Task01 {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        /**
         * 生成一个队列
         * 1、队列名称
         * 2、队列里面的消息是否持久化（磁盘） 默认消息存储在内存中
         * 3、该队列是否只供一个消费者进行消费 是否进行消息共享，true可以多个消费者消费 false：只能一个消费者消费
         * 4、是否自动删除 最后一个消费者短开连接 该队一句是否自动删除 true自动删除false不自动删除
         * 5、其它参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //从控制台当中接受消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            /**
             * 发送一个消息
             * 1、发送到那个交换机
             * 2、路由的key值是哪个 本次队列的名称
             * 3、其它参数信息
             * 4、发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成："+ message);
        }





    }


}
