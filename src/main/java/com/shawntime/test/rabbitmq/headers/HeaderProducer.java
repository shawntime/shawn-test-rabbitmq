package com.shawntime.test.rabbitmq.headers;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by shma on 2017/5/8.
 * any:其中一个匹配，
 * all:所有的均匹配
 * 生产者匹配消费者
 */
public class HeaderProducer {

    private static final String exchange_name = "ex_log_headers";

    public static void main(String[] args) throws IOException, TimeoutException {
        HeaderProducer headerProducer = new HeaderProducer();
        Map<String, Object> headerMap = new Hashtable<String, Object>();
        headerMap.put("username", "shma");
//        headerMap.put("password", "123456");
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.headers(headerMap);
        builder.deliveryMode(2);//持久化
        Connection connection = headerProducer.getConnection();
        Channel channel = headerProducer.getChannel(connection);
        channel.basicQos(1);
        channel.basicPublish(exchange_name, "", builder.build(), UUID.randomUUID().toString().getBytes());

        channel.close();
        connection.close();
    }

    public Channel getChannel(Connection connection) throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchange_name, "headers", true, false, null);
        return channel;
    }

    public Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("TEST");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setUsername("shawntime");
        connectionFactory.setPassword("shawntime");
        connectionFactory.setPort(AMQP.PROTOCOL.PORT);
        return connectionFactory.newConnection();
    }
}
