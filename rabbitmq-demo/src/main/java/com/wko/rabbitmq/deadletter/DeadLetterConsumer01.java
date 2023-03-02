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
public class DeadLetterConsumer01 {

    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static final String NORMAL_QUEUE_NAME = "normal_queue";
    public static final String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明死信和普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        Map<String, Object> arguments = new HashMap<>();
        //设置过期时间
//        arguments.put("x-message-ttl", 10000);
        //正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //设置死信routing-key
        arguments.put("x-dead-letter-routing-key", "wko");
//        arguments.put("x-max-length", 6);

        //声明死信和普通队列
        channel.queueDeclare(NORMAL_QUEUE_NAME, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE_NAME, false, false, false, null);

        //绑定
        channel.queueBind(NORMAL_QUEUE_NAME, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE_NAME, DEAD_EXCHANGE, "wko");

        System.out.println("等待接收。。。。。。。。。。。。");
        DeliverCallback deliverCallback = (consumerTag, msg) -> {
            String message = new String(msg.getBody(), StandardCharsets.UTF_8);
            if (message.equals("test6")) {
                System.out.println("拒绝此消息：" + message);
                //拒绝，且不放回队列
                channel.basicReject(msg.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer01接收：" + message);
                channel.basicAck(msg.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(NORMAL_QUEUE_NAME, false, deliverCallback, (CancelCallback) null);
    }
}
