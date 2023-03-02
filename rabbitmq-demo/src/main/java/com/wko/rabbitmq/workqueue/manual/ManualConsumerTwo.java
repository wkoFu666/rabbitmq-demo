package com.wko.rabbitmq.workqueue.manual;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.wko.rabbitmq.util.RabbitMqUtils;
import com.wko.rabbitmq.util.SleepUtils;

/**
 * ClassName: ManualConsumerTwo
 * Package: com.wko.rabbitmq.workqueue.manual
 * Description:
 *
 * @Author fuxt
 * @Create 2023/2/25 23:07
 * @Version 1.0
 */
public class ManualConsumerTwo {
    private static final String ACK_QUEUE_NAME="ack_queue";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C2 等待接收消息处理时间较长");
        //消息消费的时候如何处理消息
        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String message= new String(delivery.getBody());
            SleepUtils.sleep(15);
            System.out.println("接收到消息:"+message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        };
        //不公平分发
        channel.basicQos(2);

        //采用手动应答
        boolean autoAck=false;
        channel.basicConsume(ACK_QUEUE_NAME, false,deliverCallback,(consumerTag)->{
            System.out.println(consumerTag+"消费者取消消费接口回调逻辑");
        });
    }

}
