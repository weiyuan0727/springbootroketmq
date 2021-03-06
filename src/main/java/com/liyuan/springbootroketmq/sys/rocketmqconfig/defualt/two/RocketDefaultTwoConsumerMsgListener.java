package com.liyuan.springbootroketmq.sys.rocketmqconfig.defualt.two;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author weiyuan
 * @title: RocketMsgListener
 * @projectName springbootroketmq
 * @description: TODO
 * @date 2019/12/3/00322:59
 */
@Component
public class RocketDefaultTwoConsumerMsgListener implements MessageListenerConcurrently {
    private static final Logger LOG = LoggerFactory.getLogger(RocketDefaultTwoConsumerMsgListener.class) ;
    private Logger logger = LoggerFactory.getLogger(RocketDefaultTwoConsumerMsgListener.class);
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        for (int i = 0; i < list.size(); i++) {
            MessageExt msg = list.get(i);
            String topic = msg.getTopic();
            try {

                logger.info("Two"+new String(msg.getBody()));
                //System.out.println(1 / 0);//测试异常重试
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("=========消息处理异常=== ===");
                logger.error("=========消息处理异常===重试次数:" + msg.getReconsumeTimes());
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
}
