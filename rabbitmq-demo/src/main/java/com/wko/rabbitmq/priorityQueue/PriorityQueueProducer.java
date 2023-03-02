package com.wko.rabbitmq.priorityQueue;

import com.rabbitmq.client.*;
import com.wko.rabbitmq.util.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * ClassName: PriorityQueueProducter
 * Package: com.wko.rabbitmq.priorityQueue
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/28 16:34
 * @Version 1.0
 */
public class PriorityQueueProducer {

    public static final String QUEUE_NAME = "hello";

    public static final String IP = "124.71.214.111";
    public static final String USER = "admin";
    public static final String PASSWORD = "admin";

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(IP);
        connectionFactory.setUsername(USER);
        connectionFactory.setPassword(PASSWORD);

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-max-priority", 10);
            channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);

            for (int i = 1; i < 11; i++) {
                String msg = "this message:" + i;
                if (i == 5) {
                    AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                    channel.basicPublish("", QUEUE_NAME, properties, msg.getBytes());
                } else {
                    channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
                }
            }
            System.out.println("消息发送完毕！");
        }
    }
}
