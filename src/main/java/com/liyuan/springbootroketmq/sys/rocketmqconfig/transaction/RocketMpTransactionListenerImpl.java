package com.liyuan.springbootroketmq.sys.rocketmqconfig.transaction;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author weiyuan
 * @title: RocketMpTransactionListenerImpl
 * @projectName springbootroketmq
 * @description: TODO
 * @date 2019/12/5/00522:44
 */
/**
 * 本地业务处理成功返回 COMMIT_MESSAGE
 * 处理失败返回 ROLLBACK_MESSAGE
 * UNKNOW 表示未知 消息依然是prepare状态不会推送给消费者
 */
@Slf4j
public class RocketMpTransactionListenerImpl implements TransactionListener  {

    //本地业务
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("=====本地业务====");

        return LocalTransactionState.UNKNOW;//测试回查
    }
    //事务回查
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        try {
            log.info("===回查:"+new String(msg.getBody(),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /**
         * 根据具体业务返回消息的状态
         */
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
