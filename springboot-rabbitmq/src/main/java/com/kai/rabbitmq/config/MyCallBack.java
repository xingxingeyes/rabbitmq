package com.kai.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {


    @Autowired
    private RabbitTemplate rabbitTemplate;
    //注入
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 1.发消息 交换机接收到了 回调
     *  1.1 correlationData 保存回调消息的ID及相关信息
     *  1.2交换机收到消息 true
     *  1.3 cause null
     * 2.发消息 交换机接受失败了 回调
     *  2.1 correlationData 保存回调消息的ID及相关信息
     *  2.2 交换机收到消息ack = false
     *  2.3 cause 失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到了Id为：{}的消息",id);
        }else {
            log.info("交换机还未收到Id为：{}的消息,由于原因：{}",id,cause);
        }
    }

    //可以在消息传递过程中不可达目的地是将消息返回给生产者
    //只有不可达目的地的时候 才会进行回退
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息{}，被交换机{}退回，退回原因：{}，路由key{}", new String(message.getBody()), exchange, replyText, routingKey);
    }




}
