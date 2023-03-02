package com.wko.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * ClassName: DeadLetterQueueConsumer
 * Package: com.wko.rabbitmq.controller
 * Description:延迟队列 消费者
 *
 * @Author fuxt
 * @Create 2023/2/27 13:13
 * @Version 1.0
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = "QD")
    public void receiveMsg(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到死信队列信息{}", new Date().toString(), msg);


    }

}
