package com.shawntime.test.rabbitmq.rpc.rabbit;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import com.google.common.collect.Maps;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.shawntime.test.rabbitmq.rpc.IRpcService;
import com.shawntime.test.rabbitmq.rpc.RpcInvokeModel;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Created by shma on 2017/5/8.
 */
public class Client implements IRpcService {

    private Channel produceChannel;

    private Channel consumeChannel;

    private String callBackQueueName;

    private final Map<String, BlockingQueue<byte[]>> completionQueueMap;

    public Client(ConnectModel connectModel) throws IOException, TimeoutException {
        connect(connectModel);
        this.completionQueueMap = Maps.newConcurrentMap();
    }

    public byte[] call(RpcInvokeModel model) throws IOException, InterruptedException, ExecutionException {
        model.setDid(UUID.randomUUID().toString());
        model.setCallBackQueueName(callBackQueueName);
        byte[] body = SerializationUtils.serialize(model);
        BlockingQueue<byte[]> blockingQueue = new LinkedBlockingQueue<byte[]>(1);
        completionQueueMap.put(model.getDid(), blockingQueue);
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                .builder()
                .correlationId(model.getDid())
                .replyTo(callBackQueueName)
                .build();
        produceChannel.basicPublish(Constant.REQUEST_EXCHANGE_NAME, Constant.REQUEST_ROUTING_NAME, basicProperties, body);
        return blockingQueue.take();
    }

    private void connect(ConnectModel connectModel) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost(connectModel.getVirtualHost());
        factory.setPort(connectModel.getPort());
        factory.setUsername(connectModel.getUserName());
        factory.setPassword(connectModel.getPassword());
        factory.setHost(connectModel.getHost());
        Connection connection = factory.newConnection();
        produceChannel = connection.createChannel();
        consumeChannel = connection.createChannel();
        produceChannel.queueDeclare(Constant.REQUEST_QUEUE_NAME, true, false, false, null);
        produceChannel.exchangeDeclare(Constant.REQUEST_EXCHANGE_NAME, "direct");
        produceChannel.basicQos(1);
        callBackQueueName = produceChannel.queueDeclare().getQueue();
        consumeChannel.exchangeDeclare(Constant.REPLY_EXCHANGE_NAME, "direct");
        consumeChannel.queueBind(callBackQueueName, Constant.REPLY_EXCHANGE_NAME, callBackQueueName);
        consumeChannel.basicQos(1);
        consumeChannel.basicConsume(callBackQueueName, true, new DefaultConsumer(consumeChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, final byte[] body) throws IOException {
                BlockingQueue<byte[]> blockingQueue = completionQueueMap.get(properties.getCorrelationId());
                blockingQueue.add(body);
            }
        });
    }
}
