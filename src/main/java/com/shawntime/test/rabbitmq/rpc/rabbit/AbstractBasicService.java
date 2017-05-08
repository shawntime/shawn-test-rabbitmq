package com.shawntime.test.rabbitmq.rpc.rabbit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.shawntime.test.rabbitmq.rpc.rabbit.ConnectModel;

/**
 * Created by shma on 2017/5/8.
 */
public class AbstractBasicService {

    protected Channel producerChannel;

    protected Channel consumerChannel;

    private Connection connection;

    protected String queueName;

    public AbstractBasicService(ConnectModel connectModel, String queueName) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost(connectModel.getVirtualHost());
        factory.setPort(connectModel.getPort());
        factory.setUsername(connectModel.getUserName());
        factory.setPassword(connectModel.getPassword());
        factory.setHost(connectModel.getHost());
        connection = factory.newConnection();
        producerChannel = connection.createChannel();
        consumerChannel = connection.createChannel();
        this.queueName = queueName;
        producerChannel.queueDeclare(queueName, false, false, false, null);
    }

    public void close() throws IOException, TimeoutException {
        if (producerChannel != null) {
            producerChannel.close();
        }
        if (consumerChannel != null) {
            consumerChannel.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
 }
