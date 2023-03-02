package com.wko.rabbitmq.deadletter;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.wko.rabbitmq.util.RabbitMqUtils;

import java.util.UUID;

/**
 * ClassName: DeadLetterProducer
 * Package: com.wko.rabbitmq.deadletter
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/26 22:47
 * @Version 1.0
 */
public class DeadLetterProducer {

    public static final String NORMAL_EXCHANGE = "normal_exchange";


    public static final String NORMAL_QUEUE_NAME = "normal_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //死信消息
//        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
//                .expiration("10000").build();
        for (int i = 1; i < 11; i++) {
//            String msg = UUID.randomUUID() + "    " + i;
            String msg = "test" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, msg.getBytes());
            System.out.println("生产者发送消息：" + msg);
        }


    }
}
