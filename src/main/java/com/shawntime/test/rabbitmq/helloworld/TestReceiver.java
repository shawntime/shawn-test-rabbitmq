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

    private static final String QUEUE_NAME = "shawntime_queue_helloworld";

    /**
     * 消费端处理流程：
     *  1）创建ConnectionFactory
     *  2）创建Connection
     *  3）创建Channel
     *  4）声明队列（channel.queueDeclare），指定队列名称，自动删除，持久化
     *  5）创建消费者
     *  6）消费者指定消息队列（channel.basicConsume）
     */
    public static void receive() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory(); // 创建连接连接到MQ
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setUsername("shawntime");
        connectionFactory.setPassword("shawntime");
        connectionFactory.setPort(AMQP.PROTOCOL.PORT);
        connectionFactory.setVirtualHost("Test");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //创建消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[]
                    body) throws IOException {
                User user = JSON.parseObject(body, User.class);
                System.out.println("Received Message：" + user);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer); //指定消费队列
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
