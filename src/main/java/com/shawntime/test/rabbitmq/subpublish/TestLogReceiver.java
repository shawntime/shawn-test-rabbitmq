package com.shawntime.test.rabbitmq.subpublish;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * Created by shma on 2017/5/6.
 */
public abstract class TestLogReceiver {

    public static final String EXCHANGE_NAME = "shawntime_fanout_log";

    public void closeConnection(Connection connection) throws IOException {
        if (connection != null) {
            connection.close();
        }
    }

    public void closeChannel(Channel channel) throws IOException, TimeoutException {
        if (channel != null) {
            channel.close();
        }
    }

    public void receiver() throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        // 设置exchange类型为fanout
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 获取临时队列名称
//        String queueName = channel.queueDeclare().getQueue();
        String queueName = "log_queue" + "_" + new Random().nextInt(100);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "log_key");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String log = new String(body);
                parseLog(log);
            }
        };
        // 指定接收者，第二个参数为自动应答，无需手动应答
        channel.basicConsume(queueName, true, consumer);
    }

    public abstract void parseLog(String log);

    public Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setUsername("shawntime");
        connectionFactory.setPassword("shawntime");
        connectionFactory.setVirtualHost("Test");
        connectionFactory.setPort(AMQP.PROTOCOL.PORT);
        return connectionFactory.newConnection();
    }
}
