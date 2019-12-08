package com.liyuan.springbootroketmq.sys.rocketmqconfig.defualt.two;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author weiyuan
 * @title: ConsumerConfig
 * @projectName springbootroketmq
 * @description: TODO
 * @date 2019/12/3/00322:57
 */
@Configuration
@Slf4j
public class RocketMqDefaultTwoConsumerConfig {
    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.groupName}")
    private String groupName;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;
    @Resource
    private RocketDefaultTwoConsumerMsgListener msgListener;

    @Bean("defaultConsumerTwo")
    public DefaultMQPushConsumer getRocketMQConsumer() {
        /*  当消费者(不同jvm实例)都在同一台物理机上时，若指定instanceName，消费负载均衡将失效(每个实例都将消费所有消息)。
        另外，在一个jvm里模拟集群消费时，必须指定不同的instanceName，
        否则启动时会提示ConsumerGroup已存在。*/
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setInstanceName("two");//同一ip下同一jvm不可相同
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(msgListener);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        try {
            consumer.subscribe("myTestTopic", "*");

            consumer.start();
            log.info("===消费者(Two)启动====");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return consumer;
    }


    public static void main(String[] args) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("testConsomer");
        consumer.setNamesrvAddr("192.168.188.138:9876");
        consumer.setInstanceName("two");//同一ip下同一jvm不可相同

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (int i = 0; i < list.size(); i++) {
                    MessageExt msg = list.get(i);
                    String topic = msg.getTopic();
                    try {

                        System.out.println("Two"+new String(msg.getBody()));
                        //System.out.println(1 / 0);//测试异常重试
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("=========消息处理异常=== ===");
                        System.out.println("=========消息处理异常===重试次数:" + msg.getReconsumeTimes());
                        //失败次数达到3次返回成功做失败处理(记录日志等操作)
                        if (msg.getReconsumeTimes() == 3) {
                            //异常处理。。。。。
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                        //处理失败 重发
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        try {
            consumer.subscribe("myTestTopic", "*");

            consumer.start();
            log.info("===消费者(Two)启动====");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}
