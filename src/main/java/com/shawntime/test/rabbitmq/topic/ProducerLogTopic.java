package com.shawntime.test.rabbitmq.topic;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * *可以匹配一个标识符。
 * #可以匹配0个或多个标识符。
 */
public class ProducerLogTopic {

    private static final String exchange_name = "ex_log_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        ProducerLogTopic producerLogTopic = new ProducerLogTopic();
        Connection connection = null;
        Channel channel = null;
        try {
            connection = producerLogTopic.getConnection();
            channel = producerLogTopic.getChannel(connection);
            String[] routKeys = {"my.orange.god", "my.haha", "lazy.user", "haha.user"};
            for (String routKey : routKeys) {
                String msg = UUID.randomUUID().toString();
                System.out.println(routKey + " : " + msg);
                producerLogTopic.sendMsg(channel, routKey, msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            channel.close();
            connection.close();
        }
    }

    public void sendMsg(Channel channel, String routKey, String log) throws IOException {
        channel.basicPublish(exchange_name, routKey, MessageProperties.PERSISTENT_TEXT_PLAIN, log.getBytes());
    }

    public Channel getChannel(Connection connection) throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchange_name, "topic");
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
