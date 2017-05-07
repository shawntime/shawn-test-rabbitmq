package com.shawntime.test.rabbitmq.subpublish;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by shma on 2017/5/6.
 * fanout类型：广播到所有转换器说知道的消息队列中，无视routingKey
 */
public class TestLogProducer {

    public static final String EXCHANGE_NAME = "exchange_log";

    public static void main(String[] args) {

        Connection connection = null;
        Channel channel = null;
        try {
            connection = getConnection();
            channel = getChannel(connection);
            for (int i = 0; i < 10; ++i) {
                String msg = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                        + "," + UUID.randomUUID().toString();
                sendLog(msg, channel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                closeChannel(channel);
                closeConnection(connection);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

    }

    private static void sendLog(String log, Channel channel) throws IOException {
        channel.basicPublish(EXCHANGE_NAME, "", null, log.getBytes());
    }

    public static void closeConnection(Connection connection) throws IOException {
        if (connection != null) {
            connection.close();
        }
    }

    public static void closeChannel(Channel channel) throws IOException, TimeoutException {
        if (channel != null) {
            channel.close();
        }
    }

    public static Channel getChannel(Connection connection) throws IOException {
        Channel channel = connection.createChannel();
        // 设置exchange类型为fanout
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        return channel;
    }

    public static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setUsername("shawntime");
        connectionFactory.setPassword("shawntime");
        connectionFactory.setVirtualHost("TEST");
        connectionFactory.setPort(AMQP.PROTOCOL.PORT);
        return connectionFactory.newConnection();
    }
}
