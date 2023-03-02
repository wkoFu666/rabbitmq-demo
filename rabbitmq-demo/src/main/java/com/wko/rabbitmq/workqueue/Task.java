package com.wko.rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.wko.rabbitmq.util.RabbitMqUtils;

import java.util.Scanner;

/**
 * ClassName: Task
 * Package: com.wko.rabbitmq.workqueue
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/25 16:16
 * @Version 1.0
 */
public class Task {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String msg = scanner.next();
                channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
                System.out.println("消息发送完成:" + msg);
            }
        }
    }
}
