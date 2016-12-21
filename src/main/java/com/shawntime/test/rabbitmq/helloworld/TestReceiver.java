package com.shawntime.test.rabbitmq.helloworld;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.*;

/**
 * Created by IDEA
 * User: shawntime
 * Date: 2016-11-22 14:43
 * Desc: 消息队列消费端
 */
public class TestReceiver {

    private static final String QUEUE_NAME = "shawntime_helloworld";

    public static void receive() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory(); // 创建连接连接到MQ
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setUsername("shawntime");
        connectionFactory.setPassword("shawntime");
        connectionFactory.setPort(AMQP.PROTOCOL.PORT);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        QueueingConsumer consumer = new QueueingConsumer(channel); //创建消费者
        channel.basicConsume(QUEUE_NAME, true, consumer); //指定消费队列
        while (true) {
            //nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            User user = JSON.parseObject(delivery.getBody(), User.class);
            System.out.println("Received Message：" + user);
        }
    }

    public static void main(String[] args) {
        try {
            receive();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
