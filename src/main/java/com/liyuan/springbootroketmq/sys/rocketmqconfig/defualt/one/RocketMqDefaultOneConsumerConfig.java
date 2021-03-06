package com.liyuan.springbootroketmq.sys.rocketmqconfig.defualt.one;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author weiyuan
 * @title: ConsumerConfig
 * @projectName springbootroketmq
 * @description: TODO
 * @date 2019/12/3/00322:57
 */
@Configuration
@Slf4j
public class RocketMqDefaultOneConsumerConfig {
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
    private RocketDefaultOneConsumerMsgListener msgListener;

    @Bean("defaultConsumerOne")
    public DefaultMQPushConsumer getRocketMQConsumer() {

      /*  当消费者(不同jvm实例)都在同一台物理机上时，若指定instanceName，消费负载均衡将失效(每个实例都将消费所有消息)。
        另外，在一个jvm里模拟集群消费时，必须指定不同的instanceName，
        否则启动时会提示ConsumerGroup已存在。*/
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setInstanceName("one");//同一ip下同一jvm不可相同
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(msgListener);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        try {
            consumer.subscribe("myTestTopic", "*");

            consumer.start();
            log.info("===消费者(One)启动====");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return consumer;
    }
}
