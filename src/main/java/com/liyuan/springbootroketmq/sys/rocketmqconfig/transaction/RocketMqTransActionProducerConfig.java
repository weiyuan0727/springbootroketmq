package com.liyuan.springbootroketmq.sys.rocketmqconfig.transaction;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.*;

/**
 * @author weiyuan
 * @title: RocketMqTransActionProducerConfig
 * @projectName springbootroketmq
 * @description: TODO
 * @date 2019/12/5/00522:25
 */
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


    public TransactionMQProducer getTransactionProducer() {
        TransactionListener transactionListener = new RocketMpTransactionListenerImpl();
        TransactionMQProducer producer = new TransactionMQProducer(groupName);
        producer.setCheckThreadPoolMinSize(5);
        producer.setCheckThreadPoolMaxSize(20);
        producer.setCheckRequestHoldMax(20000);
        try {
            producer.start();
            producer.setTransactionListener(transactionListener);

        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return producer;
    }

}
