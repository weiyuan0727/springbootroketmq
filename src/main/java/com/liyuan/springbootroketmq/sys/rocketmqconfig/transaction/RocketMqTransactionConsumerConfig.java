package com.liyuan.springbootroketmq.sys.rocketmqconfig.transaction;

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
public class RocketMqTransactionConsumerConfig {
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
    private RocketMqTransactionConsumerMsgListener msgListener;

    @Bean("transactionConsumer")
    public DefaultMQPushConsumer getRocketMQConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("testTransactionConsumerGroup");
        consumer.setNamesrvAddr("192.168.188.138:9876");
       // consumer.setInstanceName("ttt");
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(msgListener);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);

        try {
            consumer.subscribe("myTestTransactionTopic", "*");

            consumer.start();
            log.info("===消费者(事务)启动====");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return consumer;
    }
}
