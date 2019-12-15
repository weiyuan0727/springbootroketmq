package com.liyuan.springbootroketmq.test;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("defaultProducer")
    @Autowired
    private DefaultMQProducer defaultMQProducer;
    @Qualifier("transactionProducer")
    @Autowired
    private TransactionMQProducer transactionMQProducer;
    @Autowired
    @Qualifier("defaultProducerTwo")
    private DefaultMQProducer defaultMQProducertwo;


    @RequestMapping("sendToMqDefault")
    public String sendToMq() {
        for (int x = 0; x < 100; x++) {

            try {
                Message msg = new Message("myTestTopic" /* Topic */,
                        "TagA" /* Tag */,
                        Integer.toString(x),//keys
                        ("Hello RocketMQ 1" + x).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                );
                //Call send message to deliver message to one of brokers.
                SendResult sendResult = defaultMQProducer.send(msg);
                System.out.printf("%s%n", sendResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //特别重要 @Bean创建的produser不能shutdown
        //defaultMQProducer.shutdown();
        for (int x = 0; x < 100; x++) {

            try {
                Message msg = new Message("myTestTopic" /** Topic */,
                        "TagA" /*** Tag */,
                        ("Hello RocketMQ 2 " + x).getBytes(RemotingHelper.DEFAULT_CHARSET) /** Message body */
                );
                //Call send message to deliver message to one of brokers.
                SendResult sendResult = defaultMQProducertwo.send(msg);
                System.out.printf("%s%n", sendResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "ok";
    }
    @RequestMapping("sendTransaction")
    public String sendTranactionMesgToMq() {
      for (int x = 0; x < 100; x++) {
            try {
                Message msg = new Message("myTestTransactionTopic" /** Topic */,
                        "TagA" /* Tag */,
                        ("测试事务消息").getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                );
                //Call send message to deliver message to one of brokers.
                SendResult sendResult = transactionMQProducer.sendMessageInTransaction(msg,"tq");
                System.out.printf("%s%n", sendResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            //不能shutdown @bean生成的producer
        //transactionMQProducer.shutdown();
        return "ok";
    }
}
