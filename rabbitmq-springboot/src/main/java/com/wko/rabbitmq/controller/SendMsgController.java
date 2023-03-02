package com.wko.rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * ClassName: SendMsgController
 * Package: com.wko.rabbitmq.controller
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/27 13:08
 * @Version 1.0
 */
@RestController
@Slf4j
@RequestMapping("ttl")
public class SendMsgController {

    public static final String X_EXCHANGE = "X";

    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{},发送一条信息给两个 TTL 队列:{}", new Date(), message);
        rabbitTemplate.convertAndSend(X_EXCHANGE, "XA", "消息来自 ttl 为 10S 的队列,信息内容： " + message);
        rabbitTemplate.convertAndSend(X_EXCHANGE, "XB", "消息来自 ttl 为 40S 的队列,信息内容：  " + message);
    }

    /**
     * 延迟队列实现
     */
    @GetMapping("sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsgSetTtl(@PathVariable String message, @PathVariable String ttlTime) {
        rabbitTemplate.convertAndSend(X_EXCHANGE, "XC", message, correlationData -> {
            correlationData.getMessageProperties().setExpiration(ttlTime);
            return correlationData;
        });
        log.info("当前时间：{},发送一条时长{}毫秒 TTL 信息给队列 C:{}", new Date(), ttlTime, message);
    }


    /**
     * rabbitmq插件实现延迟队列：在交换机延迟
     */
    @GetMapping("sendDelayMsg/{message}/{delayTime}")
    public void sendMsgDelayed(@PathVariable String message, @PathVariable Integer delayTime) {
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME, DELAYED_ROUTING_KEY, message,
                correlationData -> {
                    correlationData.getMessageProperties().setDelay(delayTime);
                    return correlationData;
                });
        log.info(" 当 前 时 间 ： {}, 发送一条延迟 {} 毫秒的信息给队列 delayed.queue:{}", new
                Date(), delayTime, message);
    }
}
