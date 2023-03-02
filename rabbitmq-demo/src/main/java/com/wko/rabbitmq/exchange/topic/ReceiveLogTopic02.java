package com.wko.rabbitmq.exchange.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wko.rabbitmq.util.RabbitMqUtils;

import java.nio.charset.StandardCharsets;

/**
 * ClassName: ReceiveLogTopic01
 * Package: com.wko.rabbitmq.exchange.topic
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/26 17:59
 * @Version 1.0
 */
public class ReceiveLogTopic02 {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        channel.queueDeclare("Q2", false, false, false, null);
        channel.queueBind("Q2", EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind("Q2", EXCHANGE_NAME, "lazy.#");
        System.out.println("等待接收消息。。。。。。。。。。");

        DeliverCallback deliverCallback = (consumerTag, msg) -> {
            System.out.println("Q2接收：" + consumerTag + "   " + new String(msg.getBody(), StandardCharsets.UTF_8));
            System.out.println("绑定匹配规则："+msg.getEnvelope().getRoutingKey());
        };
        channel.basicConsume("Q2", true, deliverCallback, (CancelCallback) null);
    }
}
