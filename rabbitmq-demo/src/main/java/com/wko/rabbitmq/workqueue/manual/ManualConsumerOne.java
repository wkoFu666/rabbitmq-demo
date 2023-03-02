package com.wko.rabbitmq.workqueue.manual;

import com.rabbitmq.client.*;
import com.wko.rabbitmq.util.RabbitMqUtils;
import com.wko.rabbitmq.util.SleepUtils;

/**
 * ClassName: ManualConsumer
 * Package: com.wko.rabbitmq.workqueue
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/25 22:28
 * @Version 1.0
 */
public class ManualConsumerOne {

    private static final String ACK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1 等待接收消息处理时间较短");
        //消息消费的时候如何处理消息
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            SleepUtils.sleep(1);
            System.out.println("接收到消息:" + message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        //不公平分发
        channel.basicQos(3);

        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(ACK_QUEUE_NAME, autoAck, deliverCallback, (consumerTag) -> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        });
    }

}
