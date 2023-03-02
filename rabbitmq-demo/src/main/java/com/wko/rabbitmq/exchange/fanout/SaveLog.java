package com.wko.rabbitmq.exchange.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wko.rabbitmq.util.RabbitMqUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * ClassName: SaveLog
 * Package: com.wko.rabbitmq.exchange.fanout
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/26 16:03
 * @Version 1.0
 */
public class SaveLog {
    public static final String EXCHANGE_NAME = "LOG";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        //声明临时队列
        String queueName = channel.queueDeclare().getQueue();
        //队列绑定交换机
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("将日志保存至文件...............");
        DeliverCallback deliverCallback = (consumerTag, msg) -> {
            String message = new String(msg.getBody(), StandardCharsets.UTF_8);
            File file = new File("G:\\1.txt");
            FileUtils.writeStringToFile(file, message, "UTF-8");
            System.out.println("日志保存成功！");
        };
        channel.basicConsume(queueName, true, deliverCallback, (CancelCallback) null);
    }
}
