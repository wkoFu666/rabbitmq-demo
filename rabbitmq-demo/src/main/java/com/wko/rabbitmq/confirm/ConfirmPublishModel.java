package com.wko.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.wko.rabbitmq.util.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * ClassName: ConfirmPublishSingle
 * Package: com.wko.rabbitmq.confirm
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/26 14:48
 * @Version 1.0
 */
public class ConfirmPublishModel {

    public static final int MESSAGE_COUNT = 1000;


    public static void main(String[] args) throws Exception {
        //发布1000条消息共耗时：17824ms
        //ConfirmPublishModel.publishMessageSingle();
        //发布1000条消息共耗时：275ms
        //ConfirmPublishModel.publishMessageBatch();
        //发布1000条消息共耗时：79ms
        ConfirmPublishModel.publishMessageAsync();
    }

    public static void publishMessageSingle() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启确认
        channel.confirmSelect();

        long start = System.currentTimeMillis();

        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = i + " ";
            channel.basicPublish("", queueName, null, msg.getBytes());
            //单个消息发送确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功！" + msg);
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条消息共耗时：" + (end - start) + "ms");
    }

    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启确认
        channel.confirmSelect();

        long start = System.currentTimeMillis();

        int batchSize = 100;
        int notConfirmCount = 0;
        //批量发消息,批量确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = i + " ";
            channel.basicPublish("", queueName, null, msg.getBytes());
            notConfirmCount++;
            if (notConfirmCount == batchSize) {
                channel.waitForConfirms();
                notConfirmCount = 0;
            }
        }
        if (notConfirmCount > 0) {
            channel.waitForConfirms();
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条消息共耗时：" + (end - start) + "ms");
    }

    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启确认
        channel.confirmSelect();

        long start = System.currentTimeMillis();

        //线程安全有序的一个哈希表，适用于高并发的情况
        ConcurrentSkipListMap<Long, String> recordList = new ConcurrentSkipListMap<>();

        //消息监听器:成功
        ConfirmCallback ackCallBack = (deliveryTag, multiple) -> {
            if (multiple) {
                //2、删除已经确认的消息
                ConcurrentNavigableMap<Long, String> confirmedMap = recordList.headMap(deliveryTag);
                confirmedMap.clear();
            } else {
                recordList.remove(deliveryTag);
            }
            System.out.println("消息" + deliveryTag + "发送成功");
        };
        //消息监听器:失败
        ConfirmCallback nAckCallBack = (deliveryTag, multiple) -> {
            String msg = recordList.get(deliveryTag);
            System.out.println("消息：(" + deliveryTag + ")" + msg + "发送失败");
        };


        channel.addConfirmListener(ackCallBack, nAckCallBack);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = "消息：" + i;
            channel.basicPublish("", queueName, null, msg.getBytes());
            //1、记录所有需要发送的消息
            recordList.put(channel.getNextPublishSeqNo(), msg);
        }


        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条消息共耗时：" + (end - start) + "ms");
    }
}
