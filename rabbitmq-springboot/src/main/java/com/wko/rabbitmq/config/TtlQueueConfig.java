package com.wko.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: TtlQueueConfig
 * Package: com.wko.rabbitmq.config
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/27 12:41
 * @Version 1.0
 */
@Configuration
public class TtlQueueConfig {
    public static final String X_EXCHANGE = "X";
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String DEAD_LETTER_QUEUE = "QD";


    /**
     * 声明X交换机
     */
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    /**
     * 声明Y交换机
     */
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    /**
     * 创建A队列并绑定对应的：死信交换机、路由key、以及ttl
     */
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //声明当前队列的死信路由 key
        arguments.put("x-dead-letter-routing-key", "YD");
        //声明队列的 TTL 10s
        arguments.put("x-message-ttl", 10000);

        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }


    /**
     * 将A队列与X交换机进行绑定
     */
    @Bean
    public Binding queueBindingA(@Qualifier("queueA") Queue queueA, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }


    /**
     * 创建B队列并绑定对应的：死信交换机、路由key、以及ttl
     */
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //声明当前队列的死信路由 key
        arguments.put("x-dead-letter-routing-key", "YD");
        //声明队列的 TTL 40s
        arguments.put("x-message-ttl", 40000);

        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    /**
     * 将B队列与X交换机进行绑定
     */
    @Bean
    public Binding queueBindingB(@Qualifier("queueB") Queue queueB, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }


    /**
     * 死信队列
     */
    @Bean("queueDead")
    public Queue queueDead() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    @Bean
    public Binding deadLetterBinding(@Qualifier("queueDead") Queue queueDead, @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueDead).to(yExchange).with("YD");
    }
}
