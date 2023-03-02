package com.wko.rabbitmq.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * ClassName: CallBack
 * Package: com.wko.rabbitmq.message
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/28 14:09
 * @Version 1.0
 */
@Slf4j
@Component
public class CallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {


    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 注入
     */
    @PostConstruct
    public void init() {
        //设置回退消息交给谁处理
        rabbitTemplate.setConfirmCallback(this);
        //true：
        //交换机无法将消息进行路由时，会将该消息返回给生产者
        //false：
        //如果发现消息无法进行路由，则直接丢弃
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机不管是否收到消息的一个回调方法
     *
     * @param correlationData 消息相关数据
     * @param b               ack
     * @param s               交换机是否接收到消息
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (b) {
            log.info("交换机已经收到 id 为:{}的消息", id);
        } else {
            log.info("交换机还未收到 id 为:{}消息,由于原因:{}", id, s);
        }

    }

    /**
     * 当消息无法路由的时候的回调方法
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error(" 消 息 {}, 被交换机 {} 退回，退回原因 :{}, 路 由 key:{}", new String(returnedMessage.getMessage().getBody()), returnedMessage.getMessage(),
                returnedMessage.getReplyText(), returnedMessage.getRoutingKey());
    }
}
