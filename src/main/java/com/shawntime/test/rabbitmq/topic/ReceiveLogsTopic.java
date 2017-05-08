package com.shawntime.test.rabbitmq.topic;

import java.io.IOException;
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
public abstract class ReceiveLogsTopic {

    private static final String exchange_name = "ex_log_topic";

    public void receiver() throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        // 设置exchange类型为fanout
        channel.exchangeDeclare(exchange_name, "topic");
        // 获取临时队列名称
//        String queueName = channel.queueDeclare().getQueue();
        String queueName = channel.queueDeclare().getQueue();
//        channel.queueDeclare(queueName, false, false, false, null);

        channel.queueBind(queueName, exchange_name, getRoutKey());
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String log = new String(body);
                System.out.println(log);
            }
        };
        // 指定接收者，第二个参数为自动应答，无需手动应答
        channel.basicConsume(queueName, true, consumer);
    }

    public abstract String getRoutKey();

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
