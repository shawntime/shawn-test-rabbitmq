package com.shawntime.test.rabbitmq.rpc.rabbit;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.shawntime.test.rabbitmq.rpc.JsonHelper;
import com.shawntime.test.rabbitmq.rpc.RpcInvokeModel;
import com.shawntime.test.rabbitmq.rpc.operator.bean.User;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Created by shma on 2017/5/8.
 */
public class Service {

    private Channel produceChannel;

    private Channel consumeChannel;

    private ConnectModel connectModel;

    public Service(ConnectModel connectModel) throws IOException, TimeoutException {
        this.connectModel = connectModel;
    }

    public void start() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost(connectModel.getVirtualHost());
        factory.setPort(connectModel.getPort());
        factory.setUsername(connectModel.getUserName());
        factory.setPassword(connectModel.getPassword());
        factory.setHost(connectModel.getHost());
        Connection connection = factory.newConnection();
        produceChannel = connection.createChannel();
        produceChannel.exchangeDeclare(Constant.REPLY_EXCHANGE_NAME, "direct");
        produceChannel.basicQos(1);

        consumeChannel = connection.createChannel();
        consumeChannel.queueDeclare(Constant.REQUEST_QUEUE_NAME, true, false, false, null);
        consumeChannel.exchangeDeclare(Constant.REQUEST_EXCHANGE_NAME, "direct");
        consumeChannel.basicQos(1);
        consumeChannel.queueBind(Constant.REQUEST_QUEUE_NAME, Constant.REQUEST_EXCHANGE_NAME, Constant
                .REQUEST_ROUTING_NAME);
        consumeChannel.basicConsume(Constant.REQUEST_QUEUE_NAME, true, new DefaultConsumer(consumeChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[]
                    body) throws IOException {
                RpcInvokeModel model = SerializationUtils.deserialize(body);
                Class cls;
                try {
                    cls = Class.forName(model.getClassName());
                    Object[] arguments = model.getArguments();
                    Class[] clazz = new Class[arguments.length];
                    for (int index = 0 ; index < clazz.length; ++index) {
                        clazz[index] = arguments[index].getClass();
                    }
                    Method method = cls.getDeclaredMethod(model.getMethodName(), clazz);
                    Object object = method.invoke(cls.newInstance(), arguments);
                    byte[] resultData = JsonHelper.serialize(object).getBytes("UTF-8");
                    String queueName = properties.getReplyTo();
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                            .correlationId(properties.getCorrelationId()).build();
                    produceChannel.basicPublish(Constant.REPLY_EXCHANGE_NAME, queueName, replyProps, resultData);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
