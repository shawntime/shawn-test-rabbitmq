package com.shawntime.test.rabbitmq.rpc.rabbit;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeoutException;

import com.google.common.collect.Maps;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.shawntime.test.rabbitmq.rpc.IRpcService;
import com.shawntime.test.rabbitmq.rpc.RpcInvokeModel;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Created by shma on 2017/5/8.
 */
public class Client extends AbstractBasicService implements IRpcService {

    private String callBackQueueName;

    private final Map<String, BlockingQueue<byte[]>> completionQueueMap;

    public Client(ConnectModel connectModel, String queueName) throws IOException, TimeoutException {
        super(connectModel, queueName);
        this.completionQueueMap = Maps.newConcurrentMap();
        callBackQueueName = producerChannel.queueDeclare().getQueue();
        consumerChannel.basicConsume(callBackQueueName, true, new DefaultConsumer(consumerChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, final byte[] body) throws IOException {
                BlockingQueue<byte[]> blockingQueue = completionQueueMap.get(properties.getCorrelationId());
                if (blockingQueue == null) {
                    blockingQueue = new SynchronousQueue<>();
                }
                blockingQueue.add(body);
            }
        });
    }

    public byte[] call(RpcInvokeModel model) throws IOException, InterruptedException, ExecutionException {
        model.setDid(UUID.randomUUID().toString());
        model.setCallBackQueueName(callBackQueueName);
        byte[] body = SerializationUtils.serialize(model);
        BlockingQueue<byte[]> blockingQueue = new SynchronousQueue<>();
        completionQueueMap.put(model.getDid(), blockingQueue);
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                .builder()
                .correlationId(model.getDid())
                .replyTo(callBackQueueName)
                .build();
        producerChannel.basicPublish("", queueName, basicProperties, body);
        return blockingQueue.take();
    }
}
