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
public class TestCustomer {

    private static final String QUEUE_NAME = "shawntime_helloworld";

    public static void main(String[] args) {
        receive();
    }

    public static void receive() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setUsername("shawntime");
        connectionFactory.setPassword("shawntime");
        connectionFactory.setPort(AMQP.PROTOCOL.PORT);

        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            //指定消费队列
            channel.basicConsume(QUEUE_NAME, true, consumer);
            while (true) {
                //nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                User user = JSON.parseObject(delivery.getBody(), User.class);
                System.out.println("Received Message：" + user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
