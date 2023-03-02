package com.wko.rabbitmq.exchange.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wko.rabbitmq.util.RabbitMqUtils;

import java.nio.charset.StandardCharsets;


/**
 * ClassName: ReciveLog
 * Package: com.wko.rabbitmq.exchange.fanout
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/26 15:55
 * @Version 1.0
 */
public class ReceiveLog {

    public static final String EXCHANGE_NAME = "LOG";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        //声明临时队列
        String queueName = channel.queueDeclare().getQueue();
        //队列绑定交换机
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接收消息...............");

        DeliverCallback deliverCallback = (consumerTag, msg) -> {
            System.out.println("消息发送成功！" + consumerTag + "   " + new String(msg.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(queueName, true, deliverCallback, (CancelCallback) null);
    }
}
