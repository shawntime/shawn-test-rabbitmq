package com.shawntime.test.rabbitmq.worker;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * Created by shma on 2017/5/4.
 */
public class TestReceiver {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = getConnection();
        final Channel channel = getChannel(connection);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties
                    properties, byte[] body) throws IOException {
                String msg = new String(body);
                try {
                    work(msg);
                    System.out.println(TestReceiver.class.hashCode() + ":" + msg);
                    //发送应答
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        boolean ack = false ; //打开应答机制
        channel.basicConsume(QUEUE_NAME, ack, consumer);
    }

    private static void work(String msg) throws InterruptedException {
        for (int index = 0; index < msg.length(); ++index) {
            if ('.' == msg.charAt(index)) {
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    //队列名称
    private final static String QUEUE_NAME = "shawntime.workqueue.durable";

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
        //公平转发  设置最大服务转发消息数量    只有在消费者空闲的时候会发送下一条信息。
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        return channel;
    }
}
