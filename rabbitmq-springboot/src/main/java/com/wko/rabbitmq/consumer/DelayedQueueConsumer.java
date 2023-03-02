package com.wko.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * ClassName: DelayedQueueConsumer
 * Package: com.wko.rabbitmq.controller
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/27 15:10
 * @Version 1.0
 */
@Component
@Slf4j
public class DelayedQueueConsumer {

    public static final String DELAYED_QUEUE_NAME = "delayed.queue";

    @RabbitListener(queues = DELAYED_QUEUE_NAME)
    public void receiveDelayedQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到延时队列的消息：{}", new Date(), msg);
    }

}
