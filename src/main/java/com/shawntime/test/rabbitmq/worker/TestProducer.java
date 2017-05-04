package com.shawntime.test.rabbitmq.worker;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * Created by shma on 2017/5/4.
 */
public class TestProducer {
    //队列名称
    private final static String QUEUE_NAME = "shawntime.workqueue.durable";

    public static void main(String[] args) throws IOException, TimeoutException {
        StringBuilder sb = new StringBuilder("msg");
        Connection connection = getConnection();
        Channel channel = getChannel(connection);
        for (int i=0; i<10; ++i) {
            sb.append(".");
            try {
                sendMsg(channel, sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        closeChannel(channel);
        closeConnection(connection);
    }

    public static void sendMsg(Channel channel, String msg) throws IOException, TimeoutException {
        //MessageProperties.PERSISTENT_TEXT_PLAIN 标识我们的信息为持久化的
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
        System.out.println("发送消息：" + msg);
    }

    public static void closeChannel(Channel channel) throws IOException, TimeoutException {
        if (channel != null) {
            channel.close();
        }
    }

    public static void closeConnection(Connection connection) throws IOException {
        if (connection != null) {
            connection.close();
        }
    }

    private static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("TEST");
        connectionFactory.setUsername("shawntime");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPassword("shawntime");
        connectionFactory.setPort(AMQP.PROTOCOL.PORT);
        return connectionFactory.newConnection();
    }

    private static Channel getChannel(Connection connection) throws IOException, TimeoutException {

        Channel channel = connection.createChannel();
        //设置消息持久化  RabbitMQ不允许使用不同的参数重新定义一个队列，所以已经存在的队列，我们无法修改其属性。
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        return channel;
    }
}
