package com.shawntime.test.rabbitmq.direct;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * Created by shma on 2017/5/8.
 */
public class SendLogDirect {

    private static final String exchange_name = "ex_log_direct";

    //日志分类
    private static final String[] SEVERITIES = {"info", "warning", "error"};

    public static void main(String[] args) {

        Connection connection = null;
        Channel channel = null;
        try {
            connection = getConnection();
            channel = getChannel(connection);
            for (int i = 0; i < 20; ++i) {
                String serverKey = "direct_" + getSeverities() + "_key";
                String msg = serverKey + " : " + UUID.randomUUID().toString();
                sendLog(msg, channel, serverKey);
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

    private static void sendLog(String log, Channel channel, String serverKey) throws IOException {
        channel.basicPublish(exchange_name, serverKey, MessageProperties.PERSISTENT_TEXT_PLAIN, log.getBytes());
    }

    private static String getSeverities() {
        int index = (int) (Math.random() * SEVERITIES.length);
        return SEVERITIES[index];
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
        channel.exchangeDeclare(exchange_name, "direct");
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
