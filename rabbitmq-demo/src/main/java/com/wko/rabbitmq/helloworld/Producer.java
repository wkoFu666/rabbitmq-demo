package com.wko.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * ClassName: Producer
 * Package: com.wko.producer
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/25 12:48
 * @Version 1.0
 */
public class Producer {
    public static final String QUEUE_NAME = "mirror_hello";

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


            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String msg = "Hello World! -v1";

            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println("消息发送完毕！");
        }


    }

}
