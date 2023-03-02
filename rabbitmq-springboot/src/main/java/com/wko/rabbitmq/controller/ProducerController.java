package com.wko.rabbitmq.controller;

import com.wko.rabbitmq.message.CallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: Producer
 * Package: com.wko.rabbitmq.controller
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/28 14:05
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {

    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";


    public static final String CONFIRM_ROUTING_KEY = "key1";
    public static final String CONFIRM_ROUTING_KEY_EXCLUDE = "key2";


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CallBack callBack;

    @GetMapping("sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {


        CorrelationData correlationData01 = new CorrelationData("01");
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, CONFIRM_ROUTING_KEY, message + CONFIRM_ROUTING_KEY, correlationData01);

        CorrelationData correlationData02 = new CorrelationData("02");
//        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME+123, CONFIRM_ROUTING_KEY, message + CONFIRM_ROUTING_KEY_EXCLUDE, correlationData02);

        CorrelationData correlationData03 = new CorrelationData("03");
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, CONFIRM_ROUTING_KEY_EXCLUDE, message + CONFIRM_ROUTING_KEY_EXCLUDE, correlationData03);
    }

}
