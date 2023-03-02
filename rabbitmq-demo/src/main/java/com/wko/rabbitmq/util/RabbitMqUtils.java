package com.wko.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * ClassName: RabbitMqUtils
 * Package: com.wko.rabbitmq.util
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/25 16:00
 * @Version 1.0
 */
public class RabbitMqUtils {
    public static final String IP = "";
    public static final String USER = "admin";
    public static final String PASSWORD = "admin";

    public static Channel getChannel() throws Exception {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP);
        factory.setUsername(USER);
        factory.setPassword(PASSWORD);
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }
}
