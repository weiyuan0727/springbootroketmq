package com.liyuan.springbootroketmq.sys.rocketmqconfig.defualt;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * @author weiyuan
 * @title: RocketMQProducer
 * @projectName springbootroketmq
 * @description: TODO
 * @date 2019/12/3/00322:11
 */
@Component
@Slf4j
public class RocketMQDefaultProducerConfig {


    private static final Logger LOG = LoggerFactory.getLogger(RocketMQDefaultProducerConfig.class);
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


    @Bean("defaultProducer")
    @DependsOn("defaultConsumer")
    public DefaultMQProducer getRocketMQProducer() {
        DefaultMQProducer producer;
        producer = new DefaultMQProducer(this.groupName);
        producer.setNamesrvAddr(this.namesrvAddr);
        //如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
        if (this.maxMessageSize != null) {
            producer.setMaxMessageSize(this.maxMessageSize);
        }
        if (this.sendMsgTimeout != null) {
            producer.setSendMsgTimeout(this.sendMsgTimeout);
        }
        //如果发送消息失败，设置重试次数，默认为2次
        if (this.retryTimesWhenSendFailed != null) {
            producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
        }
        try {
            producer.start();
            log.info("===生产者启动完成====");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return producer;
    }
}
