package com.wko.rabbitmq.workqueue;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wko.rabbitmq.util.RabbitMqUtils;

/**
 * ClassName: Worker
 * Package: com.wko.rabbitmq.workqueue
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/25 16:06
 * @Version 1.0
 */
public class Worker {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        DeliverCallback deliverCallback = (s, delivery) -> {
            String receiveMsg = new String(delivery.getBody());
            System.out.println("接收到的消息：" + receiveMsg);
        };

        CancelCallback cancelCallback = (consumer) -> {
            System.out.println(consumer + "消费者取消消费接口回调逻辑");
        };

        System.out.println("一号线程" + "消费者启动等待消费......");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

    }
}
