package com.liyuan.springbootroketmq.test;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weiyuan
 * @title: RocketMqProducerController
 * @projectName springbootroketmq
 * @description: TODO
 * @date 2019/12/5/00521:10
 */
@RestController
public class RocketMqProducerController {
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @RequestMapping("sendToMqDefault")
    public String sendToMq() {
        try {
            Message msg = new Message("myTestTopic" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ ").getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            //Call send message to deliver message to one of brokers.
            SendResult sendResult = defaultMQProducer.send(msg);
            System.out.printf("%s%n", sendResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
