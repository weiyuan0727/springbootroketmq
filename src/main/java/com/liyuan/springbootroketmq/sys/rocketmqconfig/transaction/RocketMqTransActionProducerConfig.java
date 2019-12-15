package com.liyuan.springbootroketmq.sys.rocketmqconfig.transaction;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.*;

/**
 * @author weiyuan
 * @title: RocketMqTransActionProducerConfig
 * @projectName springbootroketmq
 * @description: TODO
 * @date 2019/12/5/00522:25
 */
@Slf4j
@Configuration
public class RocketMqTransActionProducerConfig {
    @Value("${rocketmq.producer.groupName}")
    private String groupName;//群组名 全局唯一
    @Value("${rocketmq.producer.namesrvAddr}")
    private String namesrvAddr;//地址
    @Value("${rocketmq.producer.maxMessageSize}")
    private Integer maxMessageSize;//消息包体量的最大值
    @Value("${rocketmq.producer.sendMsgTimeout}")
    private Integer sendMsgTimeout;//超时时间
    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private Integer retryTimesWhenSendFailed;//生产者重试次数

    @DependsOn("transactionConsumer")
    @Bean("transactionProducer")
    public TransactionMQProducer getTransactionProducer() {
        TransactionListener transactionListener = new RocketMpTransactionListenerImpl();
        TransactionMQProducer producer = new TransactionMQProducer(groupName);
        producer.setNamesrvAddr("192.168.188.138:9786");
        //producer.setInstanceName("tran");
        producer.setCheckThreadPoolMinSize(5);
        producer.setCheckThreadPoolMaxSize(20);
        producer.setCheckRequestHoldMax(20000);
        try {
            producer.start();
            producer.setTransactionListener(transactionListener);
            log.info("===========生产者(事务)启动完成======");


        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return producer;
    }

}
