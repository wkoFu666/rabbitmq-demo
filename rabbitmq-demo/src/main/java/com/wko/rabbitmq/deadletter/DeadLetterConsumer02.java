package com.wko.rabbitmq.deadletter;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wko.rabbitmq.util.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: DeadLetterConsumer
 * Package: com.wko.rabbitmq.deadletter
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/26 21:58
 * @Version 1.0
 */
public class DeadLetterConsumer02 {

    public static final String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明死信和普通交换机
        System.out.println("等待接收信息：。。。。。。。。。。。。。。。。");
        DeliverCallback deliverCallback = (consumerTag, msg) -> {
            System.out.println("Consumer02接收：" + new String(msg.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(DEAD_QUEUE_NAME, true, deliverCallback, (CancelCallback) null);
    }
}
