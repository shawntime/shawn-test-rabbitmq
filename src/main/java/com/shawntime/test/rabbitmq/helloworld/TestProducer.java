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

    private static final String QUEUE_NAME = "shawntime_queue_helloworld";

    private static final String EXCHANGE_NAME = "shawntime_ex_helloworld";

    private static final String ROUT_KEY = "shawntime_routkey_direct_helloworld";

    private static final String FANOUT_EXCHANGE_NAME = "shawntime_fanout_helloworld";


    /**
     * 客户端连接流程：
     *  1）创建ConnectionFactory
     *  2）创建Connection
     *  3）创建Channel
     *  4）声明队列（channel.queueDeclare），指定队列名称，自动删除，持久化
     *  5）声明Exchange（channel.exchangeDeclare），指定exchange名称和类型
     *  6）队列绑定（channel.queueBind），指定queueName，exchangeName，routKey
     *  6）发送消息（channel.basicPublish），指定exchange，routeKey，数据内容
     */
    public static void sendByDirect(Object msg) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory(); //打开连接和创建频道
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setUsername("shawntime");
        connectionFactory.setPassword("shawntime");
        connectionFactory.setPort(AMQP.PROTOCOL.PORT);
        connectionFactory.setVirtualHost("Test");
        Connection connection = connectionFactory.newConnection(); //创建连接
        Channel channel = connection.createChannel(); //创建频道
        channel.queueDeclare(QUEUE_NAME, true, false, false, null); //指定队列
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUT_KEY);
        channel.basicPublish(EXCHANGE_NAME, ROUT_KEY, null, JSON.toJSONString(msg).getBytes()); //发送消息
        channel.close();
        connection.close();

    }

    public static void sendByFanout(Object msg) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory(); //打开连接和创建频道
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setUsername("shawntime");
        connectionFactory.setPassword("shawntime");
        connectionFactory.setPort(AMQP.PROTOCOL.PORT);
        connectionFactory.setVirtualHost("Test");
        Connection connection = connectionFactory.newConnection(); //创建连接
        Channel channel = connection.createChannel(); //创建频道
        channel.queueDeclare(QUEUE_NAME, true, false, false, null); //指定队列
        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, "fanout");
        channel.queueBind(QUEUE_NAME, FANOUT_EXCHANGE_NAME, "");
        channel.basicPublish(FANOUT_EXCHANGE_NAME, "AAAAA", null, JSON.toJSONString(msg).getBytes()); //发送消息
        channel.close();
        connection.close();

    }

    public static void main(String[] args) {

        for(int i=0; i<10; ++i) {
            User user = new User();
            user.setId(i);
            user.setName("张三" + i);
            user.setExchangeType("direct");
//            user.setExchangeType("fanout");
            try {
                sendByDirect(user);
//                sendByFanout(user);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

    }
}
