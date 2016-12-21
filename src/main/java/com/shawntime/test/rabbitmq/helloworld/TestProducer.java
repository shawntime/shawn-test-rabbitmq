package com.shawntime.test.rabbitmq.helloworld;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by IDEA
 * User: shawntime
 * Date: 2016-11-22 10:53
 * Desc: 消息队列生产端
 */
public class TestProducer {

    private static final String QUEUE_NAME = "shawntime_helloworld";

    public static void send(Object msg) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory(); //打开连接和创建频道
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setUsername("shawntime");
        connectionFactory.setPassword("shawntime");
        connectionFactory.setPort(AMQP.PROTOCOL.PORT);

        Connection connection = connectionFactory.newConnection(); //创建连接
        Channel channel = connection.createChannel(); //创建频道
        channel.queueDeclare(QUEUE_NAME, false, false, false, null); //指定队列
        channel.basicPublish("", QUEUE_NAME, null, JSON.toJSONString(msg).getBytes()); //发送消息
        channel.close();
        connection.close();

    }

    public static void main(String[] args) {

        for(int i=0; i<10; ++i) {
            User user = new User();
            user.setId(i);
            user.setName("张三"+i);
            try {
                send(user);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

    }
}
