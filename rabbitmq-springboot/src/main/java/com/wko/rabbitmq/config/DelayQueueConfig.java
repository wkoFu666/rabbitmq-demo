package com.wko.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: DelayQueueConfig
 * Package: com.wko.rabbitmq.config
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/27 15:03
 * @Version 1.0
 */
@Configuration
public class DelayQueueConfig {

    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    @Bean
    public Queue delayQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    /**
     * 自定义延迟交换机
     */
    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }

    @Bean
    public Binding bindingDelayQueue(@Qualifier("delayQueue") Queue delayQueue, @Qualifier("delayExchange") CustomExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAYED_ROUTING_KEY).noargs();
    }


}
