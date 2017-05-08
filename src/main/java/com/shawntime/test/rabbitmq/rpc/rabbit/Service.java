package com.shawntime.test.rabbitmq.rpc.rabbit;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.shawntime.test.rabbitmq.rpc.RpcInvokeModel;
import com.shawntime.test.rabbitmq.rpc.operator.bean.User;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Created by shma on 2017/5/8.
 */
public class Service extends AbstractBasicService {

    public Service(ConnectModel connectModel, String queueName) throws IOException, TimeoutException {
        super(connectModel, queueName);
        producerChannel.basicConsume(queueName, true, new DefaultConsumer(producerChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                RpcInvokeModel model = SerializationUtils.deserialize(body);
                Class cls = null;
                try {
                    cls = Class.forName(model.getClassName());
                    Method setMethod = cls.getDeclaredMethod(model.getMethodName(), Integer.class);
                    User user = (User)setMethod.invoke(cls.newInstance(), model.getArguments());
                    byte[] resultData = SerializationUtils.serialize(user);
                    String routingKey = properties.getReplyTo();
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                            .correlationId(properties.getCorrelationId()).build();
                    producerChannel.basicPublish("", routingKey, replyProps, resultData);
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
