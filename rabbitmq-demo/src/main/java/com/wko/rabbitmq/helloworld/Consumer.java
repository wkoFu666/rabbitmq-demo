package com.wko.rabbitmq.helloworld;


import com.rabbitmq.client.*;

/**
 * ClassName: Consumer
 * Package: com.wko.consumer
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/25 13:15
 * @Version 1.0
 */
public class Consumer {

    public static final String QUEUE_NAME = "mirror_hello";

    public static final String IP = "124.71.214.111";
    public static final String USER = "admin";
    public static final String PASSWORD = "admin";

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(IP);
        connectionFactory.setUsername(USER);
        connectionFactory.setPassword(PASSWORD);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        System.out.println("等待接收消息.....");

        //推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (s, delivery) -> {
            System.out.println(new String(delivery.getBody()));
        };

        //取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = consumerTag -> System.out.println("消息消费被中断....");

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}

