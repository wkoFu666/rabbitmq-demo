package com.wko.rabbitmq.workqueue.manual;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.wko.rabbitmq.util.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * ClassName: ManualProducer
 * Package: com.wko.rabbitmq.workqueue.manual
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/25 23:04
 * @Version 1.0
 */
public class ManualProducer {

    private static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            //发布确认开启
            channel.confirmSelect();
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入信息");
            while (sc.hasNext()) {
                String message = sc.nextLine();
                channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("生产者发出消息" + message);
            }
        }
    }
}
