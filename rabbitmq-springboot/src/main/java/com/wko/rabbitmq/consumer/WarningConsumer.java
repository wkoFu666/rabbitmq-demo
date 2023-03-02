package com.wko.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: WarningConsumer
 * Package: com.wko.rabbitmq.consumer
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/28 16:17
 * @Version 1.0
 */
@Component
@Slf4j
public class WarningConsumer {

    public static final String WARNING_QUEUE_NAME = "warning.queue";

    @RabbitListener(queues = WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("报警发现不可路由消息：{}", msg);
    }

}
