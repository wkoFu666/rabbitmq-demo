package com.wko.rabbitmq.exchange.topic;

import com.rabbitmq.client.Channel;
import com.wko.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * ClassName: TopicLog
 * Package: com.wko.rabbitmq.exchange.topic
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/26 17:57
 * @Version 1.0
 */
public class TopicLog {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit", "被队列 Q1Q2 接收到");
        bindingKeyMap.put("lazy.orange.elephant", "被队列 Q1Q2 接收到");
        bindingKeyMap.put("quick.orange.fox", "被队列 Q1 接收到");
        bindingKeyMap.put("lazy.brown.fox", "被队列 Q2 接收到");
        bindingKeyMap.put("lazy.pink.rabbit", "虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingKeyMap.put("quick.brown.fox", "不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit", "是四个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit", "是四个单词但匹配 Q2");
        Channel channel = RabbitMqUtils.getChannel();
        bindingKeyMap.forEach((key, value) -> {
            try {
                channel.basicPublish(EXCHANGE_NAME, key, null, value.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("生产者消息发出：" + value);
        });

    }

}
