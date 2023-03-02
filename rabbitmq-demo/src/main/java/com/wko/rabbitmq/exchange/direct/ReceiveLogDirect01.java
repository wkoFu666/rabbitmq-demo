package com.wko.rabbitmq.exchange.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wko.rabbitmq.util.RabbitMqUtils;

import java.nio.charset.StandardCharsets;

/**
 * ClassName: ReceiveLogDirect01
 * Package: com.wko.rabbitmq.exchange.direct
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/26 17:41
 * @Version 1.0
 */
public class ReceiveLogDirect01 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明队列
        channel.queueDeclare("console",false,false,false,null);
        //队列绑定交换机
        channel.queueBind("console", EXCHANGE_NAME, "info");
        channel.queueBind("console", EXCHANGE_NAME, "warning");

        System.out.println("等待接收消息...............");

        DeliverCallback deliverCallback = (consumerTag, msg) -> {
            System.out.println("消息发送成功！" + consumerTag + "   " + new String(msg.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume("console", true, deliverCallback, (CancelCallback) null);

    }
}
