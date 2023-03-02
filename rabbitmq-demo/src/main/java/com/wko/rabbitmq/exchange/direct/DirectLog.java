package com.wko.rabbitmq.exchange.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.wko.rabbitmq.util.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * ClassName: DispatchLog
 * Package: com.wko.rabbitmq.exchange.fanout
 * Description:发布订阅模式
 *
 * @Author fuxt
 * @Create 2023/2/26 15:54
 * @Version 1.0
 */
public class DirectLog {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String msg = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "warning", null, msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息：" + msg);
        }


    }
}
