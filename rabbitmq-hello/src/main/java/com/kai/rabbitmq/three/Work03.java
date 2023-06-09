package com.kai.rabbitmq.three;

import com.kai.rabbitmq.utils.RabbitMqUtils;
import com.kai.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Work03 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1等待接受消息处理时间较短");
        DeliverCallback deliverCallback = (consumerTags, message)->{
            SleepUtils.sleep(1);
            System.out.println(new String(message.getBody(), "UTF-8"));
            //手动应答
            /**
             * 1、消息的标记 tag
             * 2、是否批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = consumerTags ->{
            System.out.println(consumerTags + "消费者取消消费接口回掉逻辑");
        };


        //设置不公平分发
        //int prefetchCount = 1;
        //预取值是2
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);
        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck,deliverCallback, cancelCallback);


    }
}
