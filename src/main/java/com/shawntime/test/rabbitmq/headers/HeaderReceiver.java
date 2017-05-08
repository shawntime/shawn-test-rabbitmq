package com.shawntime.test.rabbitmq.headers;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * Created by shma on 2017/5/8.
 */
public class HeaderReceiver {

    private static final String exchange_name = "ex_log_headers";

    private final static String QUEUE_NAME = "header-queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        HeaderReceiver receiver = new HeaderReceiver();
        receiver.receive();
    }

    public void receive() throws IOException, TimeoutException {
        Channel channel = getChannel(getConnection());
        Map<String, Object> headerMap = new Hashtable<String, Object>();
        headerMap.put("x-match", "any");
        headerMap.put("username", "shma");
        headerMap.put("password", "123456");

        channel.queueBind(QUEUE_NAME, exchange_name, "", headerMap);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println(message);
            }
        };
        // 指定接收者，第二个参数为自动应答，无需手动应答
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

    public Channel getChannel(Connection connection) throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchange_name, "headers", true, false, null);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
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
